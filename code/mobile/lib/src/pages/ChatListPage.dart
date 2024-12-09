import 'package:flutter/material.dart';
import '../services/api.dart';
import 'ChatPage.dart'; // Import the ChatPage

class Chat {
  final int id;
  final String comboId;

  final String contactName;
  final String message;
  final String time;

  final int senderId;
  final int receiverId;

  Chat({
    required this.id,
    required this.comboId,
    required this.contactName,
    required this.message,
    required this.time,
    required this.senderId,
    required this.receiverId,
  });
}

class ChatListPage extends StatefulWidget {
  @override
  _ChatListPage createState() => _ChatListPage();

  List<Chat> chats = [];
  bool loading = false;
  String? error = "";
}

class _ChatListPage extends State<ChatListPage> {
  List<Chat> chats = [];
  bool loading = true;
  String? error = "";

  @override
  void initState() {
    super.initState();
    fetchLastMessages(); // Fetch pets when the widget is initialized
  }

  // Function to fetch pets
  Future<void> fetchLastMessages() async {
    // String userId = await Storage.loadStoredValue('user_id') ?? ''; // Get user ID from storage
    //
    setState(() {
      // loading = true; // Set loading to true
      error = null; // Reset error message
    });

    try {
      final response = await ApiService.get('message/last');

      // response is a list of chats
      List<Chat> tempChats = response
          .map<Chat>((chat) => Chat(
                id: chat['id'],
                comboId: chat['combo_id'],
                contactName: 'Chat ${chat['combo_id']}',
                message: chat['message'],
                time: chat['time'] ?? '0 min',
                senderId: chat['sender_id'],
                receiverId: chat['receiver_id'],
              ))
          .toList();

      setState(() {
        // chats.addAll(tempChats);
        chats = tempChats; // Reverse the list
      });
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao carregar chats')),
      );
    } finally {
      setState(() {
        loading = false; // Set loading to false
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Lista de Chats'),
      ),
      body: loading
          ? Center(child: CircularProgressIndicator())
          : error != null
              ? Center(child: Text(error!))
              : ListView.builder(
                  itemCount: chats.length,
                  itemBuilder: (context, index) {
                    final chat = chats[index];
                    return Card(
                      margin: const EdgeInsets.symmetric(
                          vertical: 8.0, horizontal: 16.0),
                      child: ListTile(
                        leading: CircleAvatar(
                          child: Text(chat.contactName![
                              0]), // Display first letter of client name
                        ),
                        title: Text(chat.contactName!),
                        subtitle: Text(chat.message!),
                        trailing: Text(chat.time!),
                        onTap: () async {
                          // Navigate to ChatPage when the chat card is tapped
                          final shouldRefresh = await Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => ChatPage(
                                senderId: chat.senderId,
                                receiverId: chat.receiverId,
                                contactName: chat.contactName,
                              ), // Pass any necessary data here
                            ),
                          );

                          if (shouldRefresh == true) {
                            await fetchLastMessages();
                          }
                        },
                      ),
                    );
                  },
                ),
    );
  }
}
