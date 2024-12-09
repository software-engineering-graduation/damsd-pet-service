import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/pages/Login';
import Signup from './components/pages/Signup';
import SignupPet from "./components/pages/SignupPet";
import Home from './components/pages/Home';
import Chat from './components/pages/Chat';
import Services from './components/pages/Services';
import Appointments from './components/pages/Appointments';
import MyServices from './components/pages/MyServices';
import Account from './components/pages/Account';
import ProtectedRoutes from './components/ProtectedRoutes';
import ListPet from "./components/pages/ListPet";
import { AppProvider } from '@toolpad/core';
import logo from './assets/images/PetService_Right.png';
import EstablishmentGrade from './components/pages/EstablishmentGrade';

const App = () => {
    const [user, setUser] = useState(() => {
        const savedUser = localStorage.getItem('user');
        return savedUser ? JSON.parse(savedUser) : null;
    });

    const handleLogin = (token, userData) => {
        setUser(userData);
        localStorage.setItem('authToken', token);
        localStorage.setItem('user', JSON.stringify(userData));
    };

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem('user');
        localStorage.removeItem('authToken');
    };

    const handleUpdateUser = (userData) => {
        setUser(userData);
        localStorage.setItem('user', JSON.stringify(userData));
    }

    const navigation = user?.type === 'tutor'
        ? [
            { segment: 'pets', title: 'Pets', to: '/pets' },
            { segment: 'servicos', title: 'Serviços', to: '/servicos' },
            { segment: 'agendamentos', title: 'Agendamentos', to: '/agendamentos' },
            { segment: 'chat', title: 'Mensagens', to: '/chat' },
            { segment: 'conta', title: 'Conta', to: '/conta' },
        ]
        : [
            { segment: 'agendamentos', title: 'Agendamentos', to: '/agendamentos' },
            { segment: 'chat', title: 'Mensagens', to: '/chat' },
            { segment: 'conta', title: 'Conta', to: '/conta' },
        ];
    return (
        <AppProvider
            navigation={navigation}
            branding={{
            logo: <img src={logo} alt='PetService' />,
            title: '',
            }}
        >
            <Router>
                <Routes>
                    <Route path="/" element={<Login onLogin={handleLogin} />} />
                    <Route path="/criar-conta" element={<Signup />} />
                    {/* Você pode adicionar outras rotas aqui */}

                    <Route element={<ProtectedRoutes user={user} />}>
                        <Route path="/home" element={<Home user={user} />} />
                        <Route path='/servicos' element={<Services />} />
                        <Route path='/agendamentos' element={<Appointments user={user} />} />
                        <Route path="/chat" element={<Chat user={user} />} />
                        <Route path="/meus-servicos" element={<MyServices user={user} />} />
                        <Route path='/conta' element={<Account user={user} onLogout={handleLogout} onUpdateUser={handleUpdateUser} />} />
                        <Route path="/pets" element={<ListPet user={user}/>} />
                        <Route path="/pets/criar" element={<SignupPet user={user}/>} />
                        <Route path="/avaliacoes" element={<EstablishmentGrade />} /> 
                        {/* Você pode adicionar outras rotas protegidas aqui */}
                    </Route>
                </Routes>
            </Router>
        </AppProvider>
    );
};

export default App;
