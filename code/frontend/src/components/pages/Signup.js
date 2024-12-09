import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import TutorForm from '../organisms/TutorForm';
import EstablishmentForm from '../organisms/EstablishmentForm';
import UserTypeSelector from '../molecules/UserTypeSelector';
import Logo from '../atoms/Logo';

const Signup = () => {
    useEffect(() => {
        document.title = "Pet Service - Nova conta";
    }, []);

    const API_URL = 'https://pet-service-api-d0hxdharh6d3bgd2.eastus2-01.azurewebsites.net/';
    const navigate = useNavigate();

    const [userType, setUserType] = useState('tutor');
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
    const [password, setPassword] = useState('');
    
    const [name, setName] = useState('');
    // const [services, setServices] = useState('');
    const [cnpj, setCnpj] = useState('');
    const [workingDays, setWorkingDays] = useState([]);
    const [openingTime, setOpeningTime] = useState('09:00');
    const [closingTime, setClosingTime] = useState('18:00');
    const [showDropdown, setShowDropdown] = useState(false);
    const [image, setImage] = useState('');

    const userOptions = [
        { type: 'tutor', label: 'Tutor' },
        { type: 'empresa', label: 'Empresa' },
    ];

    const toggleDropdown = () => {
        setShowDropdown((prevState) => !prevState);
    };

    const handleCheckboxChange = (day) => {
        setWorkingDays((prevDays) =>
            prevDays.includes(day) ? prevDays.filter((d) => d !== day) : [...prevDays, day]
        );
    };

    const handleUserTypeChange = (type) => {
        setUserType(type);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        let signupData;

        if (userType === 'tutor') {
            signupData = {
                full_name: fullName,
                cpf: documentNumber,
                phone_number: phone,
                birth_date: birthDate,
                address: {
                    street,
                    street_number: streetNumber,
                    city,
                    state,
                    postal_code: postalCode,
                    neighborhood,
                    additional_information: additionalInfo,
                    region: 'sudeste',
                    uf: 'mg',
                },
                image: image,
                email,
                password,
            };
        } else if (userType === 'empresa') {
            signupData = {
                business_name: name,
                password,
                email,
                phone_number: phone,
                cnpj,
                address: {
                    street,
                    street_number: streetNumber,
                    city,
                    state,
                    postal_code: postalCode,
                    neighborhood,
                    additional_information: additionalInfo,
                    region: 'sudeste',
                    uf: 'mg',
                },
                working_hours: {
                    days_of_week: workingDays,
                    opening_time: openingTime + ':00',
                    closing_time: closingTime + ':00',
                },
                // services_provided: services.split(',').map((service) => ({
                //     category_name: service.trim(),
                //     description: service.trim(),
                // })),
            };
        }

        try {
            const endpoint = userType === 'tutor' ? 'pet-guardian/auth' : 'pet-establishment/auth';

            const response = await fetch(API_URL + endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(signupData),
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Cadastro realizado com sucesso:', result);
                alert('Cadastro realizado com sucesso!');
                navigate('/');
            } else {
                console.log('Erro ao cadastrar:', response.status);
                alert('Erro ao cadastrar.');
            }
        } catch (error) {
            console.error('Erro na requisição:', error);
            alert('Erro na conexão com o servidor.');
        }
    };

    return (
        <div className="signup-container" style={{ display:'flex', flexDirection:'column', alignItems:'center'}}>
            <Logo />
            <h1 className="signup-title">Criação de Conta</h1>

            <UserTypeSelector
                label="Tipo de usuário:"
                selectedType={userType}
                onChange={handleUserTypeChange}
                style={{ display:"flex", alignItems:'center', marginBottom:'20px' }}
                options={userOptions}
            />

            {userType === 'tutor' && (
                <TutorForm
                    initialValues={null}
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
                    password={password}
                    setPassword={setPassword}
                    image={image}
                    setImage={setImage}
                    handleSubmit={handleSubmit}
                />
            )}

            {userType === 'empresa' && (
                <EstablishmentForm
                    initialValues={null}
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
                    password={password}
                    setPassword={setPassword}
                    openingTime={openingTime}
                    setOpeningTime={setOpeningTime}
                    closingTime={closingTime}
                    setClosingTime={setClosingTime}
                    workingDays={workingDays}
                    setWorkingDays={setWorkingDays}
                    showDropdown={showDropdown}
                    toggleDropdown={toggleDropdown}
                    handleCheckboxChange={handleCheckboxChange}
                    handleSubmit={handleSubmit}
                />
            )}

            <p className="login-link">
                <Link to="/">Já possuo uma conta</Link>
            </p>
        </div>
    );
};

export default Signup;
