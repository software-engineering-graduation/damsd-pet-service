import 'package:mobile/src/services/api.dart';
import 'package:intl/intl.dart';

class PetSignup {
  String name;
  String specie;
  String race;
  String weight;
  DateTime birthDate;
  String gender;

  PetSignup({
    required this.name,
    required this.specie,
    required this.race,
    required this.weight,
    required this.birthDate,
    required this.gender,
  });

  Future<void> handleSignup() async {
    // Construct the signup data
    Map<String, dynamic> signupData = {
      'name': name,
      'specie': specie,
      'race': race,
      'weight': weight,
      'birth_date': DateFormat('yyyy-MM-dd').format(birthDate),
      'gender': gender,
    };

    // Call the API service to post the signup data
    await ApiService.post('pet', signupData);
  }

  Future<void> handleEdit(String petId) async {
    // Construct the signup data
    Map<String, dynamic> signupData = {
      'name': name,
      'specie': specie,
      'race': race,
      'weight': weight,
      'birth_date': DateFormat('yyyy-MM-dd').format(birthDate),
      'gender': gender,
    };

    // Call the API service to post the signup data
    await ApiService.put('pet/$petId', signupData);
  }
}

class PetDelete {
  String petId;

  PetDelete({
    required this.petId
  });

  Future<void> handleDelete() async {
    // Call the API service to delete the pet
    await ApiService.delete('pet', petId);
  }
}

class PetGet {
  static Future<List<dynamic>> getPets(String userId, int index, int pageSize) async {
    final response = await ApiService.get('pet/pet-guardian/$userId/$index/$pageSize');
    return response['content'];
  }

  static Future<dynamic> getPetById(String petId) async {
    final response = await ApiService.get('pet/$petId');
    return response;
  }
}