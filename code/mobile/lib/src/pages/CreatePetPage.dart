import 'package:flutter/material.dart';

import '../services/pet.dart';
import 'PetsListPage.dart';

class CreatePetPage extends StatefulWidget {
  @override
  _CreatePetPageState createState() => _CreatePetPageState();
}

class _CreatePetPageState extends State<CreatePetPage> {
  final _formKey = GlobalKey<FormState>();
  String? petName;
  String? birthDate;
  String? species;
  String? breed;
  String? weight;
  String? gender;

  Future<void> _savePet() async {
    if (_formKey.currentState!.validate()) {
      PetSignup petSignup = PetSignup(
        name: petName!,
        specie: species!,
        race: breed!,
        weight: weight!,
        birthDate: DateTime.parse('2020-01-01'),
        gender: gender!,
      );

      try {
        await petSignup.handleSignup();

        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Pet cadastrado com sucesso!')),
        );

        Navigator.pop(context, true);
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Erro ao cadastrar pet')),
        );
      }


      // Process the data (e.g., send to API)
      print('Pet Name: $petName');
      print('Birth Date: $birthDate');
      print('Species: $species');
      print('Breed: $breed');
      print('Weight: $weight');
      print('Gender: $gender');
      // // Show a success message or navigate back
      // ScaffoldMessenger.of(context).showSnackBar(
      //   SnackBar(content: Text('Pet registered successfully!')),
      // );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Cadastro de Pet'),
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
                onChanged: (value) {
                  petName = value;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
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
                onChanged: (value) {
                  birthDate = value;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
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
                onChanged: (value) {
                  species = value;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
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
                onChanged: (value) {
                  breed = value;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
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
                onChanged: (value) {
                  weight = value;
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
                  backgroundColor: Colors.purple, // Use backgroundColor instead of primary
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
