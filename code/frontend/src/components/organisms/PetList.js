import React, {useState} from "react";
import {Avatar, Box, IconButton, List, ListItem, ListItemAvatar, ListItemText,} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import type {PopconfirmProps} from 'antd';
import {message, Popconfirm} from 'antd';
import apiService from "../../services/apiService";
import EditPetModal from "./EditPetModal";

const confirm: PopconfirmProps['onConfirm'] = async (id) => {
    await apiService.delete(`pet`, id);
    message.success('Pet deletado. :(');
    window.location.reload();
};

const cancel: PopconfirmProps['onCancel'] = (e) => {
    message.error('Cancelado');
};

const PetList = ({pets}) => {
    const [modalOpen, setModalOpen] = useState(false);
    const [selectedPet, setSelectedPet] = useState(null);

    const handleOpen = (pet) => {
        setSelectedPet(pet);
        setModalOpen(true);
    };

    const handleClose = () => {
        setModalOpen(false);
        setSelectedPet(null);
    };

    return (
        <List>
            {pets?.map((pet) => (
                <ListItem
                    key={pet.id}
                    sx={{display: "flex", justifyContent: "space-between"}}>
                    <Box sx={{display: "flex", alignItems: "center", cursor: "pointer"}}
                         onClick={() => handleOpen(pet)}>
                        <ListItemAvatar>
                            <Avatar src={pet.image || undefined}>
                                {!pet.image && pet.name.charAt(0).toUpperCase()}
                            </Avatar>
                        </ListItemAvatar>
                        <ListItemText primary={pet.name}/>
                    </Box>
                    <Box>
                        <IconButton aria-label="edit" onClick={() => handleOpen(pet)}>
                            <EditIcon/>
                        </IconButton>
                        <Popconfirm
                            title="Apagar o pet"
                            description="Tem certeza que deseja apagar esse pet?"
                            onConfirm={() => confirm(pet.id)}
                            onCancel={cancel}
                            okText="Sim"
                            cancelText="Não"
                        >
                            <IconButton aria-label="delete">
                                <DeleteIcon/>
                            </IconButton>
                        </Popconfirm>

                    </Box>
                </ListItem>
            ))}
            <EditPetModal open={modalOpen} onClose={handleClose} pet={selectedPet}/>
        </List>
    );
};

export default PetList;
