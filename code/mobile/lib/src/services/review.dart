import 'api.dart';

class Review {
 // Cria uma nova avaliação
  static Future<void> createReview(Map<String, dynamic> reviewData) async {
    try {
      await ApiService.post('review', reviewData);
    } catch (e) {
      print('Erro ao criar avaliação: $e');
      throw e;
    }
  }

  // Obtém uma avaliação pelo ID
  static Future<Map<String, dynamic>> getReviewById(int id) async {
    try {
      return await ApiService.get('review/$id');
    } catch (e) {
      print('Erro ao obter avaliação: $e');
      throw e;
    }
  }

  // Atualiza uma avaliação pelo ID
  static Future<void> updateReview(int id, Map<String, dynamic> reviewData) async {
    try {
      await ApiService.put('review/$id', reviewData);
    } catch (e) {
      print('Erro ao atualizar avaliação: $e');
      throw e;
    }
  }

  // Deleta uma avaliação pelo ID
  static Future<void> deleteReview(int id) async {
    try {
      await ApiService.delete('review', id.toString());
    } catch (e) {
      print('Erro ao deletar avaliação: $e');
      throw e;
    }
  }

  // Obtém uma lista paginada de avaliações
  static Future<List<dynamic>> getReviews(int index, int itemsPerPage) async {
    try {
      return await ApiService.get('review/$index/$itemsPerPage');
    } catch (e) {
      print('Erro ao obter avaliações: $e');
      throw e;
    }
  }

  // Obtém avaliações para um usuário ou pet específico, com paginação
  static Future<List<dynamic>> getReviewsForUserOrPet(int id, int index, int itemsPerPage) async {
    try {
      return await ApiService.get('review/$id/$index/$itemsPerPage');
    } catch (e) {
      print('Erro ao obter avaliações de usuário/pet: $e');
      throw e;
    }
  }
}