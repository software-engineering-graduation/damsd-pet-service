import 'package:flutter/material.dart';
import '../services/api.dart';
import '../services/message-broker.dart';
import '../storage.dart';

class Message {
  final int userId;
  final String message;

  Message({required this.userId, required this.message});

  @override
  String toString() {
    return 'Message(userId: $userId, message: "$message")';
  }
}

List<Message> parseMessages(Map<String, dynamic> jsonMap) {
  List<Message> messages = [];

  jsonMap.forEach((key, value) {
    // Split the value into userId and message
    final parts = value.split(': ');
    if (parts.length == 2) {
      final userId = int.parse(parts[0]); // Parse userId as int
      final message = parts[1]; // Get the message text
      messages.add(Message(userId: userId, message: message)); // Add to the list
    }
  });

  return messages;
}

class ChatPage extends StatefulWidget {
  final int senderId;      // Sender ID
  final int receiverId;    // Receiver ID
  final String contactName; // Contact Name

  ChatPage({
    Key? key,
    required this.senderId,
    required this.receiverId,
    required this.contactName,
  }) : super(key: key);

  @override
  _ChatPageState createState() => _ChatPageState();

  List<Message> messages = [];
}

class _ChatPageState extends State<ChatPage> {
  final ScrollController _scrollController = ScrollController();

  final List<Message> messages = [];

  final TextEditingController _messageController = TextEditingController();
  late MessageBroker messageBroker;

  late int senderId;
  late int receiverId;

  late int firstId;
  late int secondId;

  @override
  void initState() {
    super.initState();
    fetchMessages();
  }

  Future<void> fetchMessages() async {
    firstId = widget.senderId < widget.receiverId ? widget.senderId : widget.receiverId;
    secondId = widget.senderId > widget.receiverId ? widget.senderId : widget.receiverId;

    final String actualSenderId = await Storage.loadStoredValue('user_id') as String ?? '';
    final String actualReceiverId = firstId.toString() == actualSenderId ? secondId.toString() : firstId.toString();

    senderId = int.parse(actualSenderId);
    receiverId = int.parse(actualReceiverId);

    try {
      final response = await ApiService.get('message/${firstId}-${secondId}');
      List<Message> messagesTemp = parseMessages(response);

      setState(() {
        messages.addAll(messagesTemp);
      });
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao carregar serviços')),
      );
    } finally {
      WidgetsBinding.instance!.addPostFrameCallback((_) {
        _scrollToBottom();
      });
    }

    messageBroker = MessageBroker('https://dev-pet-service.onrender.com/ws'); // Use your server URL
    // Connect to the broker with sample remetente and destinatario
    messageBroker.connect(actualSenderId, actualReceiverId, (message) {
      setState(() {
        messages.add(Message(userId: receiverId, message: message)); // Add received message
      });

      WidgetsBinding.instance!.addPostFrameCallback((_) {
        _scrollToBottom();
      });
    });
  }

  void sendMessage() {
    final messageText = _messageController.text;
    if (messageText.isNotEmpty) {
      messageBroker.sendMessage(senderId.toString(), receiverId.toString(), messageText); // Use appropriate remetente and destinatario
      setState(() {
        messages.add(Message(userId: senderId, message: messageText)); // Add sent message
        _messageController.clear(); // Clear the input field
      });

      WidgetsBinding.instance!.addPostFrameCallback((_) {
        _scrollToBottom();
      });
    }
  }

  void _scrollToBottom() {
    if (_scrollController.hasClients) {
      _scrollController.jumpTo(_scrollController.position.maxScrollExtent);
    }
  }

  @override
  void dispose() {
    messageBroker.disconnect(); // Disconnect on dispose
    _scrollController.dispose(); // Dispose the ScrollController
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.contactName),
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context, true); // Go back to the previous page
          },
        ),
        actions: [
          IconButton(
            icon: Icon(Icons.more_vert),
            onPressed: () {
              // Handle settings or options
            },
          ),
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView.builder(
              controller: _scrollController,
              itemCount: messages.length,
              itemBuilder: (context, index) {
                final message = messages[index];
                return Container(
                  margin: EdgeInsets.symmetric(vertical: 4, horizontal: 8),
                  alignment: message.userId == senderId ? Alignment.centerRight : Alignment.centerLeft,
                  child: Column(
                    crossAxisAlignment: message.userId == senderId ? CrossAxisAlignment.end : CrossAxisAlignment.start,
                    children: [
                      Container(
                        padding: EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: message.userId == senderId ? Colors.purple[200] : Colors.grey[300],
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          message.message!,
                          style: TextStyle(color: Colors.black),
                        ),
                      ),
                      SizedBox(height: 2),
                      Text(
                        'Ontem', // You can replace this with the actual timestamp
                        style: TextStyle(fontSize: 12, color: Colors.grey),
                      ),
                    ],
                  ),
                );
              },
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                IconButton(
                  icon: Icon(Icons.add),
                  onPressed: () {
                    // Handle adding attachments
                  },
                ),
                IconButton(
                  icon: Icon(Icons.attach_file),
                  onPressed: () {
                    // Handle file attachment
                  },
                ),
                Expanded(
                  child: TextField(
                    controller: _messageController,
                    decoration: InputDecoration(
                      hintText: 'Escreva uma mensagem...',
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                    ),
                  ),
                ),
                IconButton(
                  icon: Icon(Icons.mic),
                  onPressed: () {
                    // Handle voice message
                  },
                ),
                IconButton(
                  icon: Icon(Icons.send),
                  onPressed: sendMessage, // Call sendMessage on press
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
