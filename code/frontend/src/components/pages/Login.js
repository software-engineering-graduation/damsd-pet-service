import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import './Login.scss';
import authService from '../../services/authService';
import InputField from '../atoms/InputField';
import Form from '../molecules/Form';
import Button from '../atoms/Button';
import Logo from '../atoms/Logo';
import UserTypeSelector from '../molecules/UserTypeSelector';

const Login = ({ onLogin }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [emailError, setEmailError] = useState('');
    const [passwordError, setPasswordError] = useState('');
    const [userType, setUserType] = useState('tutor');
    const navigate = useNavigate();

    const userOptions = [
        { type: 'tutor', label: 'Tutor' },
        { type: 'empresa', label: 'Empresa' },
    ];

    const handleUserTypeChange = (type) => {
        setUserType(type);
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        setEmailError('');
        setPasswordError('');

        if (!email.includes('@')) {
            setEmailError('Email inválido');
            return;
        }
        if (password.length < 5) {
            setPasswordError('A senha deve ter pelo menos 5 caracteres');
            return;
        }

        try {
            const response = await authService.login({ email, password, userType });
            const userResponse = await authService.getUserData(userType, response.token);
 
            const short_name = userType === "tutor" ?
                userResponse.full_name.split(' ')[0] :
                userResponse.business_name.split(' ')[0];
 
            onLogin(response.token, {...userResponse, type: userType, short_name: short_name});

            navigate('/home');
        } catch (error) {
            console.error('Erro de login:', error);
            alert('Credenciais inválidas. Revise suas credenciais de acesso e tente novamente.');
            setEmailError('Credenciais inválidas');
        }
    };

    return (
        <div className="login-container">
            <Logo />
            <Form onSubmit={handleLogin} alignment='center'>
                <InputField
                    label="Email"
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    error={emailError}
                    onClear={() => setEmail('')}
                />
                <InputField
                    label="Senha"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    error={passwordError}
                    onClear={() => setPassword('')}
                />
                <UserTypeSelector
                    label="Tipo de usuário:"
                    selectedType={userType}
                    onChange={handleUserTypeChange}
                    style={{ display:"flex", alignItems:'center', marginBottom:'20px' }}
                    options={userOptions}
                />
                <Button onClick={handleLogin} alignment="center" radius='50px' marginTop='50px'>
                    Entrar
                </Button>
            </Form>
            <p className="create-account-link">
                <Link to="/criar-conta">Criar conta</Link>
            </p>
        </div>
    );
};

Login.propTypes = {
    onLogin: PropTypes.func.isRequired,
};

export default Login;
