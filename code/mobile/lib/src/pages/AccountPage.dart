import 'package:flutter/material.dart';
import 'package:mobile/src/pages/LoginPage.dart';
import 'package:mobile/src/pages/PetsListPage.dart';
import 'package:mobile/src/storage.dart';

import '../services/auth.dart';

class AccountPage extends StatefulWidget {
  @override
  _AccountPage createState() => _AccountPage();
}

class _AccountPage extends State<AccountPage> {
  String _userName = 'Nome do Cliente';

  @override
  void initState() {
    super.initState();

    Storage.loadStoredValue('user_name').then((value) {
      if (value != null && value.isNotEmpty) {
        setState(() {
          _userName = value;
        });
      } else {
        _getUserData();
      }
    });
  }

  void _getUserData() {
    AuthService.getUserData('tutor').then((data) {
      print('User data: $data');
      setState(() {
        _userName = data['full_name'];
      });
      Storage.saveValue('user_name', data['full_name']);
    }).catchError((error) {
      print('Error getting user data: $error');
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Conta'),
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // User Info Section
            Row(
              children: [
                CircleAvatar(
                  radius: 30,
                  backgroundColor: Colors.purple[100],
                  child: Icon(
                    Icons.person,
                    color: Colors.purple,
                    size: 30,
                  ),
                ),
                SizedBox(width: 16),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      _userName,
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    Text(
                      'Tutor',
                      style: TextStyle(
                        color: Colors.grey,
                      ),
                    ),
                  ],
                ),
              ],
            ),
            SizedBox(height: 20),
            // Divider
            Divider(),
            SizedBox(height: 20),
            // Options List
            ListTile(
              title: Text('Lista de Pets'),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => PetsListPage()),
                );
              },
            ),
            ListTile(
              title: Text('Sair', style: TextStyle(color: Colors.red)),
              onTap: () {
                // Handle logout
                Storage.removeValue('token');
                Storage.removeValue('user_name');
                Navigator.of(context).pushAndRemoveUntil(
                  MaterialPageRoute(builder: (context) => LoginPage()),
                      (Route<dynamic> route) => false,
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
