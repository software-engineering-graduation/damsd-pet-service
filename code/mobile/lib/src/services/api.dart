import 'dart:convert';
import 'package:http/http.dart' as http;

import '../storage.dart';

class ApiService {
  // Base URL for the API
  static String _url(String uri) {
    return "https://dev-pet-service.onrender.com/$uri";
  }

  // Get headers, including the authorization token
  static Future<Map<String, String>> _headers() async {
    String? authToken = await Storage.loadStoredValue('token');

    return {
      'Authorization': 'Bearer $authToken',
      'Content-Type': 'application/json',
    };
  }

  // GET request
  static Future<dynamic> get(String uri) async {
    try {
      final response = await http.get(
        Uri.parse(_url(uri)),
        headers: await _headers(),
      );

      if (response.statusCode == 200) {
        return json.decode(response.body);
      } else {
        throw Exception('Failed to load data');
      }
    } catch (error) {
      print("Error during GET: $error");
      throw error;
    }
  }

  // POST request
  static Future<dynamic> post(String uri, Map<String, dynamic> data) async {
    try {
      final response = await http.post(
        Uri.parse(_url(uri)),
        headers: await _headers(),
        body: json.encode(data),
      );

      if (response.statusCode == 200 || response.statusCode == 201) {
        return json.decode(response.body);
      } else {
        throw Exception('Failed to post data');
      }
    } catch (error) {
      print("Error during POST: $error");
      throw error;
    }
  }

  // DELETE request
  static Future<dynamic> delete(String uri, String id) async {
    try {
      final response = await http.delete(
        Uri.parse(_url('$uri/$id')),
        headers: await _headers(),
      );

      if (response.statusCode == 200) {
        return json.decode(response.body);
      } else if (response.statusCode == 204) {
        return null;
      } else {
        throw Exception('Failed to delete data');
      }
    } catch (error) {
      print("Error during DELETE: $error");
      throw error;
    }
  }

  // PUT request
  static Future<dynamic> put(String uri, Map<String, dynamic> data) async {
    try {
      final response = await http.put(
        Uri.parse(_url(uri)),
        headers: await _headers(),
        body: json.encode(data),
      );

      if (response.statusCode == 200) {
        return json.decode(response.body);
      } else if (response.statusCode == 204) {
        return null;
      } else {
        throw Exception('Failed to update data');
      }
    } catch (error) {
      print("Error during PUT: $error");
      throw error;
    }
  }
}
