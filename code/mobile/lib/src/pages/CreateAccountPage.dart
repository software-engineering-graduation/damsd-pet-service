import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:mask_text_input_formatter/mask_text_input_formatter.dart';

class SignupForm extends StatefulWidget {
  final String userType;

  const SignupForm({Key? key, required this.userType}) : super(key: key);

  @override
  _SignupFormState createState() => _SignupFormState();
}

class _SignupFormState extends State<SignupForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> formData = {};
  final Map<String, MaskTextInputFormatter> maskFormatters = {
    'cpf': MaskTextInputFormatter(
        mask: '###.###.###-##', filter: {"#": RegExp(r'[0-9]')}),
    'cnpj': MaskTextInputFormatter(
        mask: '##.###.###/####-##', filter: {"#": RegExp(r'[0-9]')}),
    'phone': MaskTextInputFormatter(
        mask: '(##) #####-####', filter: {"#": RegExp(r'[0-9]')}),
  };

  String get userType => widget.userType;

  String? validateRequired(String? value) {
    if (value == null || value.isEmpty) {
      return 'Este campo é obrigatório.';
    }
    return null;
  }

  Future<void> _handleSubmit() async {
    if (!_formKey.currentState!.validate()) return;

    _formKey.currentState!.save();

    // Remover máscaras usando os formatadores
    final String unmaskedCpf = formData['documentNumber'] != null
        ? maskFormatters['cpf']!.getUnmaskedText()
        : '';
    final String unmaskedPhone =
        maskFormatters['phone']!.getUnmaskedText();
    final String? unmaskedCnpj = userType == 'empresa'
        ? maskFormatters['cnpj']!.getUnmaskedText()
        : null;

    // Validações de tamanho
    if (unmaskedCpf.length != 11 ||
        unmaskedPhone.length != 11 ||
        (userType == 'empresa' && (unmaskedCnpj?.length != 14))) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("CPF e Telefone devem ter 11 dígitos e o CNPJ 14."),
        ),
      );
      return;
    }

    // Atualizar formData com os valores sem máscara
    formData['documentNumber'] = unmaskedCpf;
    formData['phone'] = unmaskedPhone;
    if (userType == 'empresa') {
      formData['cnpj'] = unmaskedCnpj;
    }

    // Criar payload
    final signupData = userType == 'tutor'
        ? {
            "full_name": formData['fullName'],
            "cpf": formData['documentNumber'],
            "phone_number": formData['phone'],
            "email": formData['email'],
            "password": formData['password'],
          }
        : {
            "business_name": formData['businessName'],
            "cnpj": formData['cnpj'],
            "phone_number": formData['phone'],
            "email": formData['email'],
            "password": formData['password'],
          };

    // Enviar para o servidor
    try {
      final url = Uri.parse(
          'https://dev-pet-service.onrender.com/${userType == 'tutor' ? 'pet-guardian/auth' : 'pet-establishment/auth'}');
      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json'},
        body: json.encode(signupData),
      );

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Cadastro realizado com sucesso!")),
        );
        Navigator.pop(context);
      } else {
        throw Exception('Erro ao realizar cadastro: ${response.body}');
      }
    } catch (error) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro: $error')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: SingleChildScrollView(
        child: Column(
          children: [
            if (userType == 'tutor')
              TextFormField(
                decoration: InputDecoration(labelText: 'Nome completo'),
                onSaved: (value) => formData['fullName'] = value,
                validator: validateRequired,
              ),
            if (userType == 'empresa')
              TextFormField(
                decoration: InputDecoration(labelText: 'Razão social'),
                onSaved: (value) => formData['businessName'] = value,
                validator: validateRequired,
              ),
            TextFormField(
              decoration: InputDecoration(
                  labelText: userType == 'tutor' ? 'CPF' : 'CNPJ'),
              inputFormatters: [
                userType == 'tutor'
                    ? maskFormatters['cpf']!
                    : maskFormatters['cnpj']!
              ],
              validator: validateRequired,
            ),
            TextFormField(
              decoration: InputDecoration(labelText: 'Telefone'),
              inputFormatters: [maskFormatters['phone']!],
              validator: validateRequired,
            ),
            TextFormField(
              decoration: InputDecoration(labelText: 'Email'),
              validator: validateRequired,
              onSaved: (value) => formData['email'] = value,
            ),
            TextFormField(
              decoration: InputDecoration(labelText: 'Senha'),
              obscureText: true,
              validator: validateRequired,
              onSaved: (value) => formData['password'] = value,
            ),
            ElevatedButton(
              onPressed: _handleSubmit,
              child: Text('Cadastrar'),
            ),
          ],
        ),
      ),
    );
  }
}
