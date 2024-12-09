import 'package:flutter/material.dart';
import 'package:mobile/src/services/auth.dart';
import '../storage.dart';
import 'ServiceInfoPage.dart';

class ServiceListPage extends StatefulWidget {
  @override
  _ServiceListPageState createState() => _ServiceListPageState();
}

class _ServiceListPageState extends State<ServiceListPage> {
  List<dynamic> services = [];
  bool loading = false;
  String? error;
  int page = 0;
  int totalPages = 0;
  int totalElements = 0;
  final int itemsPerPage = 10;

  @override
  void initState() {
    super.initState();
    fetchServices();
  }

  Future<void> fetchServices() async {
    setState(() {
      loading = true;
      error = null;
    });

    try {
      final response = await AuthService.getAllServices(page, itemsPerPage);
      setState(() {
        services = response['content'];
        totalPages = response['page']['size'];
        totalElements = response['page']['totalElements'];
      });
    } catch (e) {
      setState(() {
        error = "Erro ao carregar serviços";
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao carregar serviços')),
      );
    } finally {
      setState(() {
        loading = false;
      });
    }
  }

  double calculateAverageRating(List<dynamic>? reviews) {
    if (reviews == null || reviews.isEmpty) return 0.0;
    double totalRating = reviews.fold(0, (sum, review) => sum + (review['rating'] ?? 0));
    return totalRating / reviews.length;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Lista de Serviços"),
      ),
      body: loading
          ? Center(child: CircularProgressIndicator())
          : error != null
              ? Center(child: Text(error!))
              : ListView.builder(
                  itemCount: services.length,
                  itemBuilder: (context, index) {
                    final service = services[index];
                    final reviews = service['reviews'] as List<dynamic>?;
                    final averageRating = calculateAverageRating(reviews);

                    return ListTile(
                      title: Text(service['name'] ?? 'Nome do Serviço'),
                      subtitle: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('${service['category_name']} • R\$ ${service['value']}'),
                          Row(
                            children: [
                              Icon(Icons.star, color: Colors.amber, size: 16),
                              SizedBox(width: 4),
                              Text(averageRating.toStringAsFixed(1)), // Exibe a média das notas
                            ],
                          ),
                        ],
                      ),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => ServiceInfoPage(service: service),
                          ),
                        );
                      },
                    );
                  },
                ),
    );
  }
}
