import 'package:flutter/material.dart';
import 'package:mobile/src/pages/ServiceListPage.dart';
import 'AccountPage.dart';
import 'ChatListPage.dart';

class MenubarBasePage extends StatefulWidget {
  @override
  _MenubarBasePage createState() => _MenubarBasePage();
}

class _MenubarBasePage extends State<MenubarBasePage> {
  int _selectedIndex = 0;

  // List of widgets to display for each tab
  final List<Widget> _widgetOptions = <Widget>[
    ServiceListPage(),
    ChatListPage(),
    AccountPage(),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: _widgetOptions.elementAt(_selectedIndex),
      ),
      bottomNavigationBar: BottomNavigationBar(
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(
            icon: Icon(Icons.list),
            label: 'Serviços',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.message),
            label: 'Mensagens',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.person),
            label: 'Conta',
          ),
        ],
        currentIndex: _selectedIndex,
        selectedItemColor: Colors.blue[800],
        onTap: _onItemTapped,
      ),
    );
  }
}
