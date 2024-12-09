import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../services/pet.dart';

class EditPetPage extends StatefulWidget {
  final String petId; // Receive the petId

  EditPetPage({required this.petId});

  @override
  _EditPetPageState createState() => _EditPetPageState();
}

class _EditPetPageState extends State<EditPetPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _petNameController = TextEditingController();
  final TextEditingController _birthDateController = TextEditingController();
  final TextEditingController _speciesController = TextEditingController();
  final TextEditingController _breedController = TextEditingController();
  final TextEditingController _weightController = TextEditingController();
  String? gender;

  @override
  void initState() {
    super.initState();
    _loadPetDetails(); // Load pet details when the page is initialized
  }

  Future<void> _loadPetDetails() async {
    // Fetch the pet details using the petId
    final pet = await PetGet.getPetById(widget.petId); // Implement this method in your PetService

    // Convert pet['birthDate'] to a string in the format 'dd/MM/yyyy'
    String birthDate = pet['birth_date'];
    List<String> dateParts = birthDate.split('-');
    String formattedDate = '${dateParts[2]}/${dateParts[1]}/${dateParts[0]}';

    // Populate the fields with the pet's current details
    setState(() {
      _petNameController.text = pet['name'];
      _birthDateController.text = formattedDate;
      _speciesController.text = pet['specie'];
      _breedController.text = pet['race'];
      _weightController.text = pet['weight'].toString();
      gender = pet['gender'];
    });
  }

  Future<void> _savePet() async {
    if (_formKey.currentState!.validate()) {
      try {
        DateTime date = DateFormat('dd/MM/yyyy').parse(_birthDateController.text);

        PetSignup petSignup = PetSignup(
          name: _petNameController.text,
          specie: _speciesController.text,
          race: _breedController.text,
          weight: _weightController.text,
          birthDate: date,
          gender: gender!,
        );

        await petSignup.handleEdit(widget.petId); // Implement this method in your PetSignup class

        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Pet atualizado com sucesso!')),
        );

        Navigator.pop(context, true); // Go back to the previous page
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Erro ao atualizar pet')),
        );
      }
    }
  }

  @override
  void dispose() {
    // Dispose of the controllers when the widget is removed from the widget tree
    _petNameController.dispose();
    _birthDateController.dispose();
    _speciesController.dispose();
    _breedController.dispose();
    _weightController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Editar Pet'),
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context); // Go back to the previous page
          },
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              TextFormField(
                controller: _petNameController, // Use the controller
                decoration: InputDecoration(
                  labelText: 'Nome',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira o nome do pet';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
                controller: _birthDateController, // Use the controller
                decoration: InputDecoration(
                  labelText: 'Data de Nascimento',
                  hintText: 'dd/mm/yyyy',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira a data de nascimento';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
                controller: _speciesController, // Use the controller
                decoration: InputDecoration(
                  labelText: 'Espécie',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira a espécie do pet';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
                controller: _breedController, // Use the controller
                decoration: InputDecoration(
                  labelText: 'Raça',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira a raça do pet';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
                controller: _weightController, // Use the controller
                decoration: InputDecoration(
                  labelText: 'Peso em kg',
                  hintText: 'Peso em kg',
                  border: OutlineInputBorder(),
                ),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira o peso do pet';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              Text('Gênero'),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Expanded(
                    child: RadioListTile<String>(
                      title: const Text('Fêmea'),
                      value: 'female',
                      groupValue: gender,
                      onChanged: (value) {
                        setState(() {
                          gender = value;
                        });
                      },
                    ),
                  ),
                  Expanded(
                    child: RadioListTile<String>(
                      title: const Text('Macho'),
                      value: 'male',
                      groupValue: gender,
                      onChanged: (value) {
                        setState(() {
                          gender = value;
                        });
                      },
                    ),
                  ),
                ],
              ),
              SizedBox(height: 16),
              ElevatedButton(
                onPressed: _savePet,
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.purple,
                  padding: EdgeInsets.symmetric(vertical: 16),
                ),
                child: Center(
                  child: Text(
                    'Salvar',
                    style: TextStyle(color: Colors.white),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
