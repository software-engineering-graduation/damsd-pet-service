import 'package:flutter/material.dart';

import '../storage.dart';
import 'ChatPage.dart';
import 'ReviewPage.dart';

class ServiceInfoPage extends StatelessWidget {
  final Map<String, dynamic> service;

  ServiceInfoPage({required this.service});

  Future<int> _getAuthorId() async {
    String? userId = await Storage.loadStoredValue('user_id');
    return int.parse(userId ?? '0');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Informações"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              service['name'] ?? 'Nome do Serviço',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 8),
            Text('${service['category_name']} • R\$ ${service['value']}',
                style: TextStyle(fontSize: 18)),
            SizedBox(height: 8),
            Text(service['description'] ?? '', style: TextStyle(fontSize: 16)),
            SizedBox(height: 24),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => ChatPage(
                      senderId: 1,
                      receiverId: 2,
                      contactName: 'Cliente 1',
                    ),
                  ),
                );
              },
              child: Text('Enviar Mensagem'),
            ),
            SizedBox(height: 16),
            ElevatedButton(
              onPressed: () async {
                int authorId = await _getAuthorId();
                int reviewedUserId = service['user_id'] ?? 0;

                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => ReviewPage(
                      authorId: authorId,
                      reviewedUserId: reviewedUserId,
                    ),
                  ),
                );
              },
              child: Text('Avaliar'),
            ),
          ],
        ),
      ),
    );
  }
}
