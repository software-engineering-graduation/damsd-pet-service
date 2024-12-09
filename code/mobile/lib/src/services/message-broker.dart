// lib/message_broker.dart

import 'package:sockjs_client_wrapper/sockjs_client_wrapper.dart';
import 'package:stomp_dart_client/stomp_dart_client.dart';

class MessageBroker {
  late StompClient _stompClient;
  late SockJSClient _sockJSClient;

  MessageBroker(String url) {
    _sockJSClient = SockJSClient(Uri.parse(url));
  }

  void connect(String remetente, String destinatario, Function(String) onMessageReceived) {
    _stompClient = StompClient(
      config: StompConfig.sockJS(
        url: 'https://dev-pet-service.onrender.com/ws', // Use your server URL
        onConnect: (StompFrame frame) {
          print('Connected: ${frame.headers}');
          _stompClient.subscribe(
            destination: '/topic/messages/$remetente/$destinatario',
            callback: (frame) {
              onMessageReceived(frame.body ?? 'No body');
            },
          );
        },
        onStompError: (StompFrame frame) {
          print('Stomp error: ${frame.body}');
        },
        onWebSocketError: (dynamic error) {
          print('WebSocket error: $error');
        },
      ),
    );

    _sockJSClient.onOpen.listen((_) {
      _stompClient.activate();
    });

    _sockJSClient.onClose.listen((_) {
      _stompClient.deactivate();
    });

    // _sockJSClient.onError.listen((error) {
    //   print('SockJS error: ${error.message}');
    // });
  }

  void sendMessage(String remetente, String destinatario, String message) {
    if (_stompClient.connected) {
      _stompClient.send(
        destination: '/app/chat/send/$remetente/$destinatario',
        body: message,
      );
    } else {
      print('Client is not connected');
    }
  }

  void disconnect() {
    _stompClient.deactivate();
    _sockJSClient.close();
  }
}
