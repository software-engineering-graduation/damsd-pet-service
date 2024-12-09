import React, { useState, useEffect } from 'react';
import { Modal, Box, Button } from '@mui/material';
import { DatePicker, TimePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { ptBR } from 'date-fns/locale';
import SelectField from '../atoms/SelectField';

const ScheduleModal = ({ isOpen, onClose, onScheduleSubmit, services, pets, initialData }) => {
    const [selectedService, setSelectedService] = useState('');
    const [selectedPet, setSelectedPet] = useState('');
    const [date, setDate] = useState(null);
    const [time, setTime] = useState(null);
    const [isDisabled, setIsDisabled] = useState(true);

    const now = new Date();

    useEffect(() => {
        if (initialData) {
            setSelectedService(initialData.service?.id || '');
            setSelectedPet(initialData.pet?.id || '');
            setDate(initialData.date ? new Date(initialData.date) : null);
            setTime(initialData.time ? new Date(initialData.time) : null);
        }
    }, [initialData]);

    useEffect(() => {
        if (initialData) {
            const isUnchanged =
                selectedService && selectedService === (initialData?.service?.id || '') &&
                selectedPet && selectedPet === (initialData?.pet?.id || '') &&
                date && date?.toISOString() === (initialData?.date ? new Date(initialData.date).toISOString() : null) &&
                time && time?.toISOString() === (initialData?.time ? new Date(initialData.time).toISOString() : null);
    
            setIsDisabled(isUnchanged);
        } else {
            const isComplete = selectedService && selectedPet && date && time;

            setIsDisabled(!isComplete);
        }
    }, [selectedService, selectedPet, date, time, initialData]);

    const handleSubmit = () => {
        if (selectedService && selectedPet && date && time) {

            const service = services.find((service) => service.id === selectedService);
            const pet = pets.find((pet) => pet.id === selectedPet);

            const scheduleData = {
                service: {
                    id: service.id,
                    name: service.name,
                    value: service.value
                },
                pet: {
                    id: pet.id,
                    name: pet.name
                },
                date,
                time,
                status: 'requested',
                appointment_id: initialData?.appointment_id || null
            };

            onScheduleSubmit(scheduleData);
            onClose();
        }
    };

    return (
        <Modal open={isOpen} onClose={onClose}>
            <Box
                sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: 400,
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    borderRadius: '8px',
                }}
            >
                <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={ptBR}>
                    <h2 style={{ marginTop: 0, textAlign: 'center' }}>Agendar Serviço</h2>
                    <SelectField
                        label="Serviço"
                        value={selectedService}
                        onChange={(e) => setSelectedService(e.target.value)}
                        options={services.map((service) => ({
                            key: service.id,
                            value: service.id,
                            label: service.name
                        }))}
                    />
                    <SelectField
                        label="Pet"
                        value={selectedPet}
                        onChange={(e) => setSelectedPet(e.target.value)}
                        options={pets.map((pet) => ({
                            key: pet.id,
                            value: pet.id,
                            label: pet.name,
                        }))}
                    />
                    <DatePicker
                        label="Data"
                        value={date}
                        minDate={now}
                        onChange={(newValue) => setDate(newValue)}
                        sx={{ marginBottom: "16px", width: "100%" }}
                    />
                    <TimePicker
                        label="Hora"
                        value={time}
                        minTime={date && date.toDateString() === now.toDateString() ? now : undefined}
                        onChange={(newValue) => setTime(newValue)}
                        ampm={false}
                        sx={{ marginBottom: "16px", width: "100%" }}
                    />
                    <Button
                        onClick={handleSubmit}
                        variant="contained"
                        color="primary"
                        fullWidth
                        disabled={isDisabled}
                    >
                        {initialData ? 'Remarcar' : 'Agendar'}
                    </Button>
                </LocalizationProvider>
            </Box>
        </Modal>
    );
};

export default ScheduleModal;
