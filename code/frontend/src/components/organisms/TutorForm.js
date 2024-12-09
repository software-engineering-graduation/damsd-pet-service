import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import InputField from "../atoms/InputField";
import Button from "../atoms/Button";
import Form from "../molecules/Form";
import TermsAcceptance from "../molecules/TermsAcceptance";
import SelectField from "../atoms/SelectField";
import ImageUpload from "../atoms/ImageUpload";

const TutorForm = ({
    initialValues = {},
    fullName, setFullName,
    documentNumber, setDocumentNumber,
    phone, setPhone,
    birthDate, setBirthDate,
    street, setStreet,
    streetNumber, setStreetNumber,
    additionalInfo, setAdditionalInfo,
    neighborhood, setNeighborhood,
    city, setCity,
    state, setState,
    postalCode, setPostalCode,
    email, setEmail,
    password, setPassword,
    image, setImage,
    handleSubmit,
    isEditing = false
}) => {
  const [acceptTerms, setAcceptTerms] = useState(false);

    useEffect(() => {
        if (initialValues) {
            setFullName(initialValues.full_name || '');
            setDocumentNumber(initialValues.document_number || '');
            setPhone(initialValues.phone_number || '');
            setBirthDate(initialValues.birth_date || '');
            setStreet(initialValues.address.street || '');
            setStreetNumber(initialValues.address.street_number || '');
            setAdditionalInfo(initialValues.address.additional_information || '');
            setNeighborhood(initialValues.address.neighborhood || '');
            setCity(initialValues.address.city || '');
            setState(initialValues.address.state || '');
            setPostalCode(initialValues.address.postal_code || '');
            setEmail(initialValues.email || '');
            setImage(initialValues.image || '');
        }
    }, [initialValues, setFullName, setDocumentNumber, setPhone, setBirthDate, setStreet, setStreetNumber, setAdditionalInfo, setNeighborhood, setCity, setState, setPostalCode, setEmail, setImage]);

    return (
        <Form onSubmit={handleSubmit} style={{ width:'100%', maxWidth:'400px' }}>
            <InputField
                label="Nome Completo"
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
            />
            {!isEditing && (
                <InputField
                    label="CPF"
                    value={documentNumber}
                    onChange={(e) => setDocumentNumber(e.target.value)}
                />
            )}
            <InputField
                label="Telefone"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
            />
            <InputField
                type="date"
                label="Data de Nascimento"
                value={birthDate}
                onChange={(e) => setBirthDate(e.target.value)}
                InputLabelProps={{
                  style: { top: "-0.5rem" },
                }}
            />
            <InputField
                label="Logradouro"
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
            <SelectField
              label={"Estado"}
              onChange={(e) => setState(e.target.value)}
              value={state}
              options={states}
            />
            <InputField
                label="CEP"
                value={postalCode}
                onChange={(e) => setPostalCode(e.target.value)}
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
            <ImageUpload
                label="Foto de Perfil"
                value={image}
                onChange={(e) => setImage(e)}
            />
            {!isEditing && (
                <TermsAcceptance
                    onChange={() => setAcceptTerms(!acceptTerms)}
                    label="Eu aceito os termos e condições de uso da plataforma."
                />
            )}
            <Button type="submit" radius="50px" marginTop="20px" disabled={!acceptTerms && !isEditing}>
                {isEditing ? 'Salvar' : 'Criar conta'}
            </Button>
        </Form>
    );
};

TutorForm.propTypes = {
    initialValues: PropTypes.object,
    fullName: PropTypes.string.isRequired,
    setFullName: PropTypes.func.isRequired,
    documentNumber: PropTypes.string.isRequired,
    setDocumentNumber: PropTypes.func.isRequired,
    phone: PropTypes.string.isRequired,
    setPhone: PropTypes.func.isRequired,
    birthDate: PropTypes.string.isRequired,
    setBirthDate: PropTypes.func.isRequired,
    street: PropTypes.string.isRequired,
    setStreet: PropTypes.func.isRequired,
    streetNumber: PropTypes.string.isRequired,
    setStreetNumber: PropTypes.func.isRequired,
    additionalInfo: PropTypes.string,
    setAdditionalInfo: PropTypes.func.isRequired,
    neighborhood: PropTypes.string.isRequired,
    setNeighborhood: PropTypes.func.isRequired,
    city: PropTypes.string.isRequired,
    setCity: PropTypes.func.isRequired,
    state: PropTypes.string.isRequired,
    setState: PropTypes.func.isRequired,
    postalCode: PropTypes.string.isRequired,
    setPostalCode: PropTypes.func.isRequired,
    email: PropTypes.string.isRequired,
    setEmail: PropTypes.func.isRequired,
    handleSubmit: PropTypes.func.isRequired,
    isEditing: PropTypes.bool
};

const states = [
  { value: "Acre", key: "AC", label: "AC - Acre" },
  { value: "Alagoas", key: "AL", label: "AL - Alagoas" },
  { value: "Amapá", key: "AP", label: "AP - Amapá" },
  { value: "Amazonas", key: "AM", label: "AM - Amazonas" },
  { value: "Bahia", key: "BA", label: "BA - Bahia" },
  { value: "Ceará", key: "CE", label: "CE - Ceará" },
  { value: "Espírito Santo", key: "ES", label: "ES - Espírito Santo" },
  { value: "Goiás", key: "GO", label: "GO - Goiás" },
  { value: "Maranhão", key: "MA", label: "MA - Maranhão" },
  { value: "Mato Grosso", key: "MT", label: "MT - Mato Grosso" },
  { value: "Mato Grosso do Sul", key: "MS", label: "MS - Mato Grosso do Sul" },
  { value: "Minas Gerais", key: "MG", label: "MG - Minas Gerais" },
  { value: "Pará", key: "PA", label: "PA - Pará" },
  { value: "Paraíba", key: "PB", label: "PB - Paraíba" },
  { value: "Paraná", key: "PR", label: "PR - Paraná" },
  { value: "Pernambuco", key: "PE", label: "PE - Pernambuco" },
  { value: "Piauí", key: "PI", label: "PI - Piauí" },
  { value: "Rio de Janeiro", key: "RJ", label: "RJ - Rio de Janeiro" },
  { value: "Rio Grande do Norte", key: "RN", label: "RN - Rio Grande do Norte" },
  { value: "Rio Grande do Sul", key: "RS", label: "RS - Rio Grande do Sul" },
  { value: "Rondônia", key: "RO", label: "RO - Rondônia" },
  { value: "Roraima", key: "RR", label: "RR - Roraima" },
  { value: "Santa Catarina", key: "SC", label: "SC - Santa Catarina" },
  { value: "São Paulo", key: "SP", label: "SP - São Paulo" },
  { value: "Sergipe", key: "SE", label: "SE - Sergipe" },
  { value: "Tocantins", key: "TO", label: "TO - Tocantins" },
];
export default TutorForm;
