import React, { useState } from 'react';
import { Box, Typography, Divider, Avatar, Link, Tooltip } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import DashboardLayout from '../templates/DashboardLayout';
import TutorForm from '../organisms/TutorForm';
import EstablishmentForm from '../organisms/EstablishmentForm';
import EditIcon from '@mui/icons-material/Edit';
import authService from '../../services/authService';

const Account = ({ user, onLogout, onUpdateUser }) => {
    const navigate = useNavigate();
    const [isEditing, setIsEditing] = useState(false);
    const [fullName, setFullName] = useState('');
    const [documentNumber, setDocumentNumber] = useState('');
    const [phone, setPhone] = useState('');
    const [birthDate, setBirthDate] = useState('');
    const [street, setStreet] = useState('');
    const [streetNumber, setStreetNumber] = useState('');
    const [additionalInfo, setAdditionalInfo] = useState('');
    const [neighborhood, setNeighborhood] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');
    const [postalCode, setPostalCode] = useState('');
    const [email, setEmail] = useState('');

    const [name, setName] = useState('');
    // const [services, setServices] = useState('');
    const [cnpj, setCnpj] = useState("");
    const [workingDays, setWorkingDays] = useState([]);
    const [openingTime, setOpeningTime] = useState('09:00');
    const [closingTime, setClosingTime] = useState('18:00');
    const [image, setImage] = useState('');

    const handleLogout = async () => {
        onLogout();
        navigate("/");
    };

    const handleEditToggle = () => {
        setIsEditing(!isEditing);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const updateData = {type: user.type, address: {uf: 'mg', region: 'sudeste'}};

        if (fullName) updateData.full_name = fullName;
        if (documentNumber) updateData.document_number = documentNumber;
        if (phone) updateData.phone_number = phone;
        if (birthDate) updateData.birth_date = birthDate;
        if (street) updateData.address.street = street;
        if (streetNumber) updateData.address.street_number = streetNumber;
        if (additionalInfo)
            updateData.address.additional_information = additionalInfo;
        if (neighborhood) updateData.address.neighborhood = neighborhood;
        if (city) updateData.address.city = city;
        if (state) updateData.address.state = state;
        if (postalCode) updateData.address.postal_code = postalCode;
        if (email) updateData.email = email;
        if (image) updateData.image = image;

        if (name) updateData.business_name = name;
        // if (services) updateData.services = services;
        if (cnpj) updateData.cnpj = cnpj;
        if (user.type !== "tutor") {
            updateData.working_hours = {};
            if (workingDays) updateData.working_hours.days_of_week = workingDays;
            if (openingTime) updateData.working_hours.opening_time = openingTime;
            if (closingTime) updateData.working_hours.closing_time = closingTime;
        }

        try {
            const response = await authService.updateUserData(user.id, updateData)
            console.log('Dados atualizados com sucesso:', response);
            alert('Dados atualizados com sucesso!');

            const userResponse = await authService.getUserData(user.type);
            const short_name = user.type === "tutor" ?
                userResponse.full_name.split(' ')[0] :
                userResponse.business_name.split(' ')[0];
 
            onUpdateUser({...userResponse, type: user.type, short_name: short_name});

            setIsEditing(false);
        } catch (error) {
            console.error('Erro ao atualizar:', error);
            alert('Erro ao atualizar os dados.');
        }
    };

    const handleCheckboxChange = (day) => {
        setWorkingDays((prevDays) =>
            prevDays.includes(day) ? prevDays.filter((d) => d !== day) : [...prevDays, day]
        );
    };

    return (
        <DashboardLayout>
            <Box sx={{ padding: '20px' }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Minha conta
                </Typography>

                {isEditing ? (
                    user.type === 'tutor' ? (
                        <TutorForm
                            initialValues={user}
                            isEditing={true}
                            fullName={fullName}
                            setFullName={setFullName}
                            documentNumber={documentNumber}
                            setDocumentNumber={setDocumentNumber}
                            phone={phone}
                            setPhone={setPhone}
                            birthDate={birthDate}
                            setBirthDate={setBirthDate}
                            street={street}
                            setStreet={setStreet}
                            streetNumber={streetNumber}
                            setStreetNumber={setStreetNumber}
                            neighborhood={neighborhood}
                            setNeighborhood={setNeighborhood}
                            city={city}
                            setCity={setCity}
                            state={state}
                            setState={setState}
                            postalCode={postalCode}
                            setPostalCode={setPostalCode}
                            additionalInfo={additionalInfo}
                            setAdditionalInfo={setAdditionalInfo}
                            email={email}
                            setEmail={setEmail}
                            image={image}
                            setImage={setImage}
                            handleSubmit={handleSubmit}
                        />
                    ) : (
                        <EstablishmentForm
                            initialValues={user}
                            isEditing={true}
                            name={name}
                            setName={setName}
                            cnpj={cnpj}
                            setCnpj={setCnpj}
                            street={street}
                            setStreet={setStreet}
                            streetNumber={streetNumber}
                            setStreetNumber={setStreetNumber}
                            neighborhood={neighborhood}
                            setNeighborhood={setNeighborhood}
                            city={city}
                            setCity={setCity}
                            state={state}
                            setState={setState}
                            postalCode={postalCode}
                            setPostalCode={setPostalCode}
                            additionalInfo={additionalInfo}
                            setAdditionalInfo={setAdditionalInfo}
                            // services={services}
                            // setServices={setServices}
                            phone={phone}
                            setPhone={setPhone}
                            email={email}
                            setEmail={setEmail}
                            openingTime={openingTime}
                            setOpeningTime={setOpeningTime}
                            closingTime={closingTime}
                            setClosingTime={setClosingTime}
                            workingDays={workingDays}
                            setWorkingDays={setWorkingDays}
                            handleCheckboxChange={handleCheckboxChange}
                            handleSubmit={handleSubmit}
                        />
                    )
                ) : (
                    <>
                        <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: '20px' }}>
                            <Avatar alt={user.type === 'tutor' ? user.full_name : user.business_name} src="/path/to/user-logo.png" sx={{ width: 56, height: 56, marginRight: 2 }} />
                            <Box>
                                <Typography variant="h6" sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                                    {user.type === 'tutor' ? user.full_name : user.business_name}&nbsp;
                                    <Tooltip title='Editar'>
                                        <EditIcon
                                            sx={{ color: '#333', fontSize: 'medium', cursor: 'pointer' }}
                                            onClick={handleEditToggle}
                                        />
                                    </Tooltip>
                                </Typography>
                                <Typography variant="body2">{user.type[0].toUpperCase() + user.type.substring(1)}</Typography>
                            </Box>
                        </Box>
                        <Divider sx={{ marginBottom: '20px' }} />
                        {user.type === 'tutor' && (
                            <Link
                                underline='none'
                                variant='h6'
                                onClick={() => navigate('/meus-pets')}
                                sx={{ color: '#333', width: '100%', display: 'block', paddingBottom: '20px', cursor: 'pointer' }}
                            >
                                Lista de Pets
                            </Link>
                        )}
                        {user.type === 'empresa' && (
                            <Link
                                underline='none'
                                variant='h6'
                                onClick={() => navigate('/meus-servicos')}
                                sx={{ color: '#333', width: '100%', display: 'block', paddingBottom: '20px', cursor: 'pointer' }}
                            >
                                Meus Serviços
                            </Link>
                        )}
                        <Link
                            underline='none'
                            variant='h6'
                            onClick={handleLogout}
                            sx={{ color: '#333', marginwidth: '100%', display: 'block', cursor: 'pointer' }}
                        >
                            Sair
                        </Link>
                    </>
                )}
            </Box>
        </DashboardLayout>
    );
};

export default Account;
