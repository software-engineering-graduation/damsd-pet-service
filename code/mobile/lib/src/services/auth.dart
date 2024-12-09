import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:mobile/src/storage.dart';

class AuthService {
  static const String _apiUrl = 'https://dev-pet-service.onrender.com/';

  // Login method
  static Future<dynamic> login(Map<String, dynamic> credentials) async {
    String endpoint = credentials['userType'] == "tutor"
        ? 'pet-guardian/auth/authenticate'
        : 'pet-establishment/auth/authenticate';

    final response = await http.post(
      Uri.parse(_apiUrl + endpoint),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(credentials),
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to login');
    }
  }

  // Get user data
  static Future<dynamic> getUserData(String userType) async {
    String? token = await Storage.loadStoredValue('token');

    String endpoint = userType == "tutor" ? 'pet-guardian/me' : 'pet-establishment/me';

    final response = await http.get(
      Uri.parse(_apiUrl + endpoint),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Error fetching user data');
    }
  }

  // Update user data
  static Future<dynamic> updateUserData(String userId, Map<String, dynamic> userData) async {
    String? token = await Storage.loadStoredValue('token');

    String endpoint = userData['type'] == "tutor"
        ? 'pet-guardian/$userId'
        : 'pet-establishment/$userId';

    final response = await http.put(
      Uri.parse(_apiUrl + endpoint),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode(userData),
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Error updating user data');
    }
  }

  // Get all services
  static Future<dynamic> getAllServices(int index, int itemsPerPage) async {
    String? token = await Storage.loadStoredValue('token');

    final response = await http.get(
      Uri.parse('${_apiUrl}service-provided/$index/$itemsPerPage'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Error fetching services');
    }
  }

  // Get establishment services
  static Future<dynamic> getEstablishmentServices(String userId, int index, int itemsPerPage) async {
    String? token = await Storage.loadStoredValue('token');

    final response = await http.get(
      Uri.parse('$_apiUrl/service-provided/pet-establishment/$userId/$index/$itemsPerPage'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Error fetching establishment services');
    }
  }

  // Get categories
  static Future<dynamic> getCategories(int index, int itemsPerPage) async {
    String? token = await Storage.loadStoredValue('token');

    final response = await http.get(
      Uri.parse('$_apiUrl/category-tag/$index/$itemsPerPage'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body)['content'];
    } else {
      throw Exception('Error fetching categories');
    }
  }

  // Get all categories
  static Future<List<dynamic>> getAllCategories() async {
    int index = 0;
    const int itemsPerPage = 50;
    List<dynamic> allCategories = [];
    bool hasMore = true;

    try {
      while (hasMore) {
        List<dynamic> categories = await getCategories(index, itemsPerPage);
        allCategories.addAll(categories);
        hasMore = categories.length == itemsPerPage;
        index++;
      }
    } catch (error) {
      print("Error fetching all categories: $error");
      throw error;
    }

    return allCategories;
  }

  // Add service
  static Future<dynamic> addService(Map<String, dynamic> newService) async {
    String? token = await Storage.loadStoredValue('token');

    final response = await http.post(
      Uri.parse('$_apiUrl/service-provided'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode(newService),
    );

    if (response.statusCode == 200 || response.statusCode == 201) {
      return json.decode(response.body);
    } else {
      throw Exception('Error adding service');
    }
  }

  // Update service
  static Future<dynamic> updateService(String serviceId, Map<String, dynamic> updatedService) async {
    String? token = await Storage.loadStoredValue('token');

    final response = await http.put(
      Uri.parse('$_apiUrl/service-provided/$serviceId'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode(updatedService),
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Error updating service');
    }
  }

  // Get appointment services by user
  static Future<dynamic> getAppointmentServicesByUser(String userType, String userId) async {
    String? token = await Storage.loadStoredValue('token');

    String endpoint = userType == "tutor"
        ? 'appointment/pet-guardian/$userId'
        : 'appointment/pet-establishment/$userId';

    final response = await http.get(
      Uri.parse('$_apiUrl/$endpoint'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Error fetching appointment services');
    }
  }
}
