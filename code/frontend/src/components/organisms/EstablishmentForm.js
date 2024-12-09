import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import InputField from '../atoms/InputField';
import Button from '../atoms/Button';
import Form from '../molecules/Form';
import { FormControlLabel, Checkbox } from '@mui/material';
import TermsAcceptance from '../molecules/TermsAcceptance';

const DAYS_OF_WEEK = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
const DIAS_DA_SEMANA = ['Segunda-feira', 'Terça-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sábado', 'Domingo'];

const EstablishmentForm = ({
    initialValues = {},
    name, setName,
    cnpj, setCnpj,
    street, setStreet,
    streetNumber, setStreetNumber,
    neighborhood, setNeighborhood,
    city, setCity,
    state, setState,
    postalCode, setPostalCode,
    additionalInfo, setAdditionalInfo,
    // services, setServices,
    phone, setPhone,
    email, setEmail,
    password, setPassword,
    closingTime, setClosingTime,
    openingTime, setOpeningTime,
    workingDays, setWorkingDays,
    handleCheckboxChange,
    handleSubmit,
    isEditing = false
}) => {
    const [acceptTerms, setAcceptTerms] = useState(false);

    useEffect(() => {
        if (initialValues) {
            setName(initialValues.business_name || '');
            setCnpj(initialValues.cnpj || '');
            setStreet(initialValues.address.street || '');
            setStreetNumber(initialValues.address.street_number || '');
            setNeighborhood(initialValues.address.neighborhood || '');
            setCity(initialValues.address.city || '');
            setState(initialValues.address.state || '');
            setPostalCode(initialValues.address.postal_code || '');
            setAdditionalInfo(initialValues.address.additional_information || '');
            // setServices(initialValues.services || '');
            setPhone(initialValues.phone_number || '');
            setEmail(initialValues.email || '');
            setOpeningTime(initialValues.working_hours.opening_time || '');
            setClosingTime(initialValues.working_hours.closing_time || '');
            setWorkingDays(initialValues.working_hours.days_of_week || []);
        }
    }, [initialValues, setName, setCnpj, setStreet, setStreetNumber, setNeighborhood, setCity, setState, setPostalCode, setAdditionalInfo,
        // setServices,
        setPhone, setEmail, setOpeningTime, setClosingTime, setWorkingDays]);

    return (
        <Form onSubmit={handleSubmit} style={{ width:'100%', maxWidth:'400px' }}>
            <InputField
                label="Nome do Estabelecimento"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />
            <InputField
                label="CNPJ"
                value={cnpj}
                onChange={(e) => setCnpj(e.target.value)}
            />
            <InputField
                label="Rua"
                value={street}
                onChange={(e) => setStreet(e.target.value)}
            />
            <InputField
                label="Número"
                value={streetNumber}
                onChange={(e) => setStreetNumber(e.target.value)}
            />
            <InputField
                label="Complemento"
                value={additionalInfo}
                onChange={(e) => setAdditionalInfo(e.target.value)}
            />
            <InputField
                label="Bairro"
                value={neighborhood}
                onChange={(e) => setNeighborhood(e.target.value)}
            />
            <InputField
                label="Cidade"
                value={city}
                onChange={(e) => setCity(e.target.value)}
            />
            <InputField
                label="Estado"
                value={state}
                onChange={(e) => setState(e.target.value)}
            />
            <InputField
                label="CEP"
                value={postalCode}
                onChange={(e) => setPostalCode(e.target.value)}
            />
            {/* <InputField
                label="Serviços Oferecidos (separados por vírgulas)"
                value={services}
                onChange={(e) => setServices(e.target.value)}
            /> */}
            <InputField
                label="Telefone"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
            />
            <InputField
                label="Email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            {!isEditing && (
                <InputField
                label="Senha"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                />
            )}

            <div className="days-select-container" style={{ display: 'flex', flexDirection: 'column', marginBottom: '20px' }}>
                <label>Dias de Funcionamento</label>
                {DAYS_OF_WEEK.map((day, index) => (
                    <FormControlLabel
                        key={index}
                        control={
                            <Checkbox
                                checked={workingDays.includes(day)}
                                onChange={() => handleCheckboxChange(day)}
                                color="primary"
                            />
                        }
                        label={DIAS_DA_SEMANA[index]}
                    />
                ))}
            </div>

            <InputField
                label="Horário de Abertura"
                type="time"
                value={openingTime}
                onChange={(e) => setOpeningTime(e.target.value)}
            />
            <InputField
                label="Horário de Fechamento"
                type="time"
                value={closingTime}
                onChange={(e) => setClosingTime(e.target.value)}
            />
            {!isEditing && (
                <TermsAcceptance
                onChange={() => setAcceptTerms(!acceptTerms)}
                label="Eu aceito os termos e condições de uso da plataforma."
                />
            )}
            <Button type="submit" radius="50px" marginTop="20px" disabled={!acceptTerms && !isEditing}>
                {isEditing ? 'Atualizar Estabelecimento' : 'Criar Estabelecimento'}
            </Button>
        </Form>
    );
};

EstablishmentForm.propTypes = {
    initialValues: PropTypes.object,
    name: PropTypes.string.isRequired,
    setName: PropTypes.func.isRequired,
    cnpj: PropTypes.string.isRequired,
    setCnpj: PropTypes.func.isRequired,
    street: PropTypes.string.isRequired,
    setStreet: PropTypes.func.isRequired,
    streetNumber: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number
    ]).isRequired,
    setStreetNumber: PropTypes.func.isRequired,
    neighborhood: PropTypes.string.isRequired,
    setNeighborhood: PropTypes.func.isRequired,
    city: PropTypes.string.isRequired,
    setCity: PropTypes.func.isRequired,
    state: PropTypes.string.isRequired,
    setState: PropTypes.func.isRequired,
    postalCode: PropTypes.string.isRequired,
    setPostalCode: PropTypes.func.isRequired,
    additionalInfo: PropTypes.string.isRequired,
    setAdditionalInfo: PropTypes.func.isRequired,
    // services: PropTypes.string.isRequired,
    // setServices: PropTypes.func.isRequired,
    phone: PropTypes.string.isRequired,
    setPhone: PropTypes.func.isRequired,
    email: PropTypes.string.isRequired,
    setEmail: PropTypes.func.isRequired,
    password: PropTypes.string,
    setPassword: PropTypes.func,
    openingTime: PropTypes.string.isRequired,
    setOpeningTime: PropTypes.func.isRequired,
    closingTime: PropTypes.string.isRequired,
    setClosingTime: PropTypes.func.isRequired,
    workingDays: PropTypes.array.isRequired,
    setWorkingDays: PropTypes.array.isRequired,
    handleCheckboxChange: PropTypes.func.isRequired,
    handleSubmit: PropTypes.func.isRequired,
    isEditing: PropTypes.bool
};

export default EstablishmentForm;
