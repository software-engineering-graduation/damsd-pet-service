import React, { useEffect, useState } from "react";
import apiService from "../../services/apiService";
import Modal from "@mui/material/Modal";
import { Box } from "@mui/material";
import PetForm from "./PetForm";

const EditPetModal = ({ open, onClose, pet }) => {
  const [name, setName] = useState("");
  const [specie, setSpecie] = useState("");
  const [race, setRace] = useState("");
  const [weight, setWeight] = useState(0);
  const [birthDate, setBirthDate] = useState("");
  const [gender, setGender] = useState("male");
  const [image, setImage] = useState("");
  const [isEditing, setIsEditing] = useState(false);


  useEffect(() => {
    if (pet) {
      setName(pet.name);
      setSpecie(pet.specie);
      setRace(pet.race);
      setWeight(pet.weight);
      setBirthDate(pet.birth_date);
      setGender(pet.gender);
      setImage(pet.image);
      setIsEditing(true);
    } else {
      setName("");
      setSpecie("");
      setRace("");
      setWeight(0);
      setBirthDate("");
      setGender("male");
      setIsEditing(false);
    }
  }, [pet]);

  const handleSignup = async () => {
    let petData = {
      name,
      specie,
      race,
      weight,
      birth_date: birthDate,
      gender,
      image
    };

    try {
      if (isEditing && pet?.id) {
        await apiService.put(`pet/${pet.id}`, petData);
        window.location.reload();
      } else {
        await apiService.post(`pet`, petData);
      }
    } catch (error) {
      console.error("Erro ao salvar pet:", error);
    }
  };

  return (
      <Modal
          open={open}
          onClose={onClose}
          aria-labelledby="modal-title"
          aria-describedby="modal-description"
      >
        <Box sx={{ ...modalStyle }}>
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
              buttonText={isEditing ? "Salvar" : "Criar"}
          />
        </Box>
      </Modal>
  );
};

const modalStyle = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4,
};

export default EditPetModal;
