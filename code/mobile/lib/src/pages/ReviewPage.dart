import 'package:flutter/material.dart';
import '../services/review.dart';

class ReviewPage extends StatefulWidget {
  final int authorId;
  final int reviewedUserId;

  ReviewPage({
    required this.authorId,
    required this.reviewedUserId,
  });

  @override
  _ReviewPageState createState() => _ReviewPageState();
}

class _ReviewPageState extends State<ReviewPage> {
  int rating = 0;
  final TextEditingController commentController = TextEditingController();

  Future<void> _submitRating() async {
    Map<String, dynamic> reviewData = {
      "author_id": widget.authorId,
      "reviewed_user_id": widget.reviewedUserId,
      "rating": rating,
      "comment": commentController.text,
    };

    try {
      await Review.createReview(reviewData);
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Avaliação enviada com sucesso!")),
      );
      Navigator.pop(context);
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Erro ao enviar avaliação")),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Avaliar Serviço"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text("Dê uma nota de 1 a 5:", style: TextStyle(fontSize: 18)),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: List.generate(5, (index) {
                return IconButton(
                  icon: Icon(
                    index < rating ? Icons.star : Icons.star_border,
                    color: Colors.amber,
                  ),
                  onPressed: () {
                    setState(() {
                      rating = index + 1;
                    });
                  },
                );
              }),
            ),
            SizedBox(height: 16),
            Text("Comentário:", style: TextStyle(fontSize: 18)),
            TextField(
              controller: commentController,
              decoration: InputDecoration(
                hintText: "Escreva seu comentário",
                border: OutlineInputBorder(),
              ),
              maxLines: 4,
            ),
            SizedBox(height: 24),
            ElevatedButton(
              onPressed: rating > 0 ? _submitRating : null,
              child: Text("Enviar Avaliação"),
            ),
          ],
        ),
      ),
    );
  }
}
