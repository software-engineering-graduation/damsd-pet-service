import React, { useEffect, useState } from "react";
import apiService from "../../services/apiService";
import PetForm from "../organisms/PetForm";
import { Box } from "@mui/material";
import DashboardLayout from "../templates/DashboardLayout";
import IconButton from "@mui/material/IconButton";
import { ArrowBack } from "@mui/icons-material";

const SignupPet = () => {
  useEffect(() => {
    document.title = "Pet Service - Cadastro do Pet";
  }, []);

  const [name, setName] = useState("");
  const [specie, setSpecie] = useState("");
  const [race, setRace] = useState("");
  const [weight, setWeight] = useState(0);
  const [birthDate, setBirthDate] = useState("");
  const [image, setImage] = useState("");
  const [gender, setGender] = useState("male");
  const handleSignup = async () => {
    let signupData = {
      name: name,
      specie: specie,
      race: race,
      weight: weight,
      birth_date: birthDate,
      gender: gender,
      image: image,
    };
    await apiService.post("pet", signupData);
  };

  return (
    <DashboardLayout>
      <Box sx={{ margin: "20px" }}>
        <IconButton href={`/pets`}>
          <ArrowBack />
        </IconButton>
        <div
          className="signup-container"
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <h1 className="signup-title">Cadastro de Pet</h1>
          <PetForm
            name={name}
            setName={setName}
            specie={specie}
            setSpecie={setSpecie}
            race={race}
            setRace={setRace}
            weight={weight}
            setWeight={setWeight}
            gender={gender}
            setGender={setGender}
            birthDate={birthDate}
            setBirthDate={setBirthDate}
            image={image}
            setImage={setImage}
            handleSignup={handleSignup}
            buttonText={"Salvar"}
          />
        </div>
      </Box>
    </DashboardLayout>
  );
};

export default SignupPet;
