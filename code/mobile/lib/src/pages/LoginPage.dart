import 'package:flutter/material.dart';
import 'package:mobile/src/storage.dart';
import 'CreateAccountPage.dart';
import 'MenubarBasePage.dart';

import 'package:mobile/src/services/auth.dart';

// Function to show an alert dialog
void _showAlertDialog(BuildContext context, String title, String message) {
  showDialog(
    context: context,
    builder: (BuildContext context) {
      return AlertDialog(
        title: Text(title),
        content: Text(message),
        actions: [
          TextButton(
            child: Text('OK'),
            onPressed: () {
              Navigator.of(context).pop(); // Close the dialog
            },
          ),
        ],
      );
    },
  );
}

class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  // Create TextEditingControllers for email and password
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  String userType = 'tutor'; // Default user type, adjust as needed

  @override
  void initState() {
    super.initState();
    _checkToken();
  }

  // Check if a token exists in storage
  Future<void> _checkToken() async {
    String? token = await Storage.loadStoredValue('token');
    if (token != null && token.isNotEmpty) {
      // If a token exists, navigate to MenubarBasePage
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (context) => MenubarBasePage()),
            (Route<dynamic> route) => false,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image.asset('assets/logo.png'),
            SizedBox(height: 32),
            TextField(
              controller: emailController, // Assign the email controller
              decoration: InputDecoration(
                labelText: 'Email',
                helperText: 'Supporting text',
              ),
            ),
            SizedBox(height: 16),
            TextField(
              controller: passwordController, // Assign the password controller
              obscureText: true,
              decoration: InputDecoration(
                labelText: 'Senha',
                helperText: 'Supporting text',
              ),
            ),
            SizedBox(height: 32),
            ElevatedButton(
              onPressed: () async {
                // Retrieve the email and password from the controllers
                String email = emailController.text;
                String password = passwordController.text;

                // Create the credentials map
                Map<String, dynamic> credentials = {
                  'userType': userType, // Adjust this based on your app logic
                  'email': email,
                  'password': password,
                };

                try {
                  // Call the login function
                  Map<String, dynamic> response = await AuthService.login(credentials);

                  await Storage.saveValue('token', response['token']);

                  Map<String, dynamic> userData = await AuthService.getUserData('tutor');
                  String userId = userData['id'].toString();
                  await Storage.saveValue('user_id', userId);
                  await Storage.saveValue('user_name', userData['full_name']);

                  Navigator.of(context).pushAndRemoveUntil(
                    MaterialPageRoute(builder: (context) => MenubarBasePage()),
                    (Route<dynamic> route) => false,
                  );
                } catch (e) {
                  print('Error during login: $e');
                  _showAlertDialog(context, 'Status do Login', 'Erro de autenticação');
                  // Show an error message to the user
                }
              },
              child: Text('Entrar'),
            ),
            TextButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => CreateAccountPage()),
                );
              },
              child: Text('Criar Conta'),
            ),
          ],
        ),
      ),
    );
  }
}
