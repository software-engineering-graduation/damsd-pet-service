import React, { useState } from "react";
import InputField from "../atoms/InputField";
import RadioGroup from "../molecules/RadioGroup";
import Button from "../atoms/Button";
import Form from "../molecules/Form";
import { Alert, AlertTitle, InputAdornment } from "@mui/material";
import ImageUpload from "../atoms/ImageUpload";

const PetForm = ({
  name,
  setName,
  specie,
  setSpecie,
  race,
  setRace,
  gender,
  setGender,
  birthDate,
  setBirthDate,
  weight,
  setWeight,
  image,
  setImage,
  handleSignup,
  errors,
  buttonText,
}) => {
  const [isCreating, setIsCreating] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setIsCreating(true);
      await handleSignup();
      setAlertMessage("Pet criado com sucesso!");
    } catch (error) {
      console.error("Error creating pet:", error);
      setAlertMessage("Erro ao criar pet. Tente novamente.");
    } finally {
      setIsCreating(false);
    }
  };

  return (
    <Form onSubmit={handleSubmit} style={{ width: "100%", maxWidth: "400px" }}>
      <InputField
        type="string"
        label="Nome"
        value={name}
        error={errors?.name}
        onChange={(e) => setName(e.target.value)}
        onClear={() => setName("")}
      />
      <InputField
        type="date"
        label="Data de Nascimento"
        value={birthDate}
        error={errors?.birthDate}
        onChange={(e) => setBirthDate(e.target.value)}
        onClear={() => setBirthDate("")}
        InputLabelProps={{
          style: { top: "-0.5rem" },
        }}
      />
      <InputField
        type="string"
        label="Espécie"
        value={specie}
        error={errors?.specie}
        onChange={(e) => setSpecie(e.target.value)}
        onClear={() => setSpecie("")}
      />
      <InputField
        type="string"
        label="Raça"
        value={race}
        error={errors?.race}
        onChange={(e) => setRace(e.target.value)}
        onClear={() => setRace("")}
      />
      <RadioGroup
        label="Gênero"
        value={gender}
        direction="column"
        options={[
          { value: "female", label: "Fêmea" },
          { value: "male", label: "Macho" },
        ]}
        error={errors?.gender}
        onChange={(e) => setGender(e.target.value)}
        onClear={() => setGender("")}
      />
      <InputField
        type="number"
        label="Peso"
        value={weight}
        error={errors?.weight}
        onChange={(e) => setWeight(e.target.value)}
        onClear={() => setWeight(0)}
        endAdornment={<InputAdornment position={"end"}>kg</InputAdornment>}
      />
      <ImageUpload
        label="Foto do Pet"
        value={image}
        error={errors?.image}
        onChange={(e) => setImage(e)}
      />
      <Button
        type="submit"
        radius="50px"
        marginTop="20px"
        disabled={isCreating}
      >
        {isCreating ? "Enviando..." : buttonText + " PET"}
      </Button>
      <br />
      {alertMessage && (
        <Alert
          severity={alertMessage.startsWith("Pet criado") ? "success" : "error"}
        >
          <AlertTitle>{alertMessage}</AlertTitle>
        </Alert>
      )}
      <br></br>
    </Form>
  );
};
export default PetForm;
