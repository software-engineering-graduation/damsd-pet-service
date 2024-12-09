import 'package:flutter/material.dart';
import 'package:mobile/src/storage.dart';
import '../services/api.dart';
import '../services/pet.dart';
import 'CreatePetPage.dart';
import 'EditPetPage.dart';

class PetsListPage extends StatefulWidget {
  @override
  _PetsListPageState createState() => _PetsListPageState();
}

class _PetsListPageState extends State<PetsListPage> {
  List<dynamic> pets = []; // List to hold pets
  int index = 0; // Pagination index
  bool loading = false; // Loading state
  String? error; // Error message
  int? page; // Current page number

  @override
  void initState() {
    super.initState();
    fetchPets(); // Fetch pets when the widget is initialized
  }

  // Function to fetch pets
  Future<void> fetchPets() async {
    String userId = await Storage.loadStoredValue('user_id') ?? ''; // Get user ID from storage

    setState(() {
      loading = true; // Set loading to true
      error = null; // Reset error message
    });

    try {
      final response = await ApiService.get('pet/pet-guardian/${userId}/$index/5');

      int pageSize = response['page']['size'];
      int pageNumber = response['page']['number'];
      int totalElements = response['page']['totalElements'];
      int totalPages = response['page']['totalPages'];

      setState(() {
        page = pageNumber; // Set current page number
        pets = response['content']; // Add new pets to the list
      });
    } catch (e) {
      setState(() {
        error = "Erro ao carregar pets"; // Set error message
      });
    } finally {
      setState(() {
        loading = false; // Set loading to false
      });
    }
  }

  // Function to handle edit action
  Future<void> _editPet(String petId) async {
    // Implement your edit logic here
    print('Edit pet with ID: $petId');

    final shouldRefresh = await Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => EditPetPage(petId: petId)),
    );

    if (shouldRefresh == true) {
      await fetchPets(); // Refresh the pet list
    }
  }

  // Function to handle delete action
  Future<void> _deletePet(String petId) async {
    // Implement your delete logic here
    print('Delete pet with ID: $petId');

    try {
      PetDelete petDelete = PetDelete(petId: petId);
      await petDelete.handleDelete();

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Pet excluído com sucesso!')),
      );

      await fetchPets();
    } catch (e) {
      print(e);
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao excluir pet')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Pets'),
      ),
      body: loading
          ? Center(child: CircularProgressIndicator()) // Show loading indicator
          : error != null
              ? Center(child: Text(error!)) // Show error message
              : ListView.builder(
                  itemCount: pets.length,
                  itemBuilder: (context, index) {
                    final pet = pets[index];
                    return Card(
                      margin: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
                      child: ListTile(
                        title: Text(pet['name'] ?? 'Nome do Pet'), // Display pet name
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            IconButton(
                              icon: Icon(Icons.edit, color: Colors.blue),
                              onPressed: () => _editPet(pet['id'].toString()), // Call edit function
                            ),
                            IconButton(
                              icon: Icon(Icons.delete, color: Colors.red),
                              onPressed: () => _deletePet(pet['id'].toString()), // Call delete function
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          // Logic to add a new pet or load more pets
          final shouldRefresh = await Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => CreatePetPage()),
          );

          if (shouldRefresh == true) {
            await fetchPets(); // Refresh the pet list
          }
        },
        backgroundColor: Colors.purple,
        child: Icon(Icons.add),
        tooltip: 'Adicionar Pet',
      ),
    );
  }
}
