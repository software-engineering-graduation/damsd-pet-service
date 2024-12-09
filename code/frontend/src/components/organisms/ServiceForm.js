import React, { useState, useEffect } from 'react';
import { Box, MenuItem, Typography } from '@mui/material';
import InputField from '../atoms/InputField';
import Button from '../atoms/Button';
import authService from '../../services/authService';
import Form from '../molecules/Form';
import LoadingSpinner from '../atoms/LoadingSpinner';

const ServiceForm = ({ setShowForm, serviceToEdit }) => {
    const [loading, setLoading] = useState(true);
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [category, setCategory] = useState('');
    const [categories, setCategories] = useState([]);
    const [value, setValue] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchAllCategories = async () => {
            setLoading(true);

            try {
                const response = await authService.getAllCategories();
                setCategories(response);

                if (serviceToEdit) {
                    setName(serviceToEdit.name || '');
                    setDescription(serviceToEdit.description || '');
                    setValue(serviceToEdit.value || '');
                    const categoryId = response.find(cat => cat.category_name === serviceToEdit.category_name)?.id || '';
                    setCategory(categoryId);
                }
            } catch (err) {
                setError(err.message || 'Erro ao carregar categorias');
            } finally {
                setLoading(false);
            }
        };

        fetchAllCategories();
    }, [serviceToEdit]);

    const handleCancel = () => {
        setShowForm(false);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        let newService = {
            name,
            description,
            category_tag: {
                category_name: categories.find(cat => cat.id === category).category_name
            },
            value
        };

        try {
            if (serviceToEdit) {
                await authService.updateService(serviceToEdit.id, newService);
            } else {
                newService.establishment_id = 3;
                await authService.addService(newService);
            }
            setShowForm(false);
        } catch (err) {
            const message = serviceToEdit ? 'Erro ao atualizar serviço' : 'Erro ao cadastrar serviço';
            err.message
                ? setError(message + ': ' + err.message)
                : setError(message);
        }
    };

    const isFormValid = () => {
        return name && description && category && value > 0;
    };

    if (loading) {
        return (
            <>
                {serviceToEdit ? (
                    <Typography variant="h6">Editar {serviceToEdit.name}</Typography>
                ) : (
                    <Typography variant="h6">Cadastrar serviço</Typography>
                )}
                <LoadingSpinner />
            </>
        );
    }

    return (
        <>
            {serviceToEdit ? (
                <Typography variant="h6">Editar {serviceToEdit.name}</Typography>
            ) : (
                <Typography variant="h6">Cadastrar serviço</Typography>
            )}
            <Form onSubmit={handleSubmit} style={{ width:'100%', maxWidth:'400px', marginInline: 'auto', marginTop: '25px' }}>
                {error && <Typography color="error">{error}</Typography>}
                <InputField
                    label="Nome do Serviço"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <InputField
                    label="Descrição"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    multiline
                    maxRows={3}
                />
                <InputField
                    label="Categoria"
                    name="category"
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    select
                    SelectProps={{ native: true }}
                    fullWidth
                    required
                    sx={{ marginBottom: 2 }}
                >
                    <MenuItem value="">Selecione uma categoria</MenuItem>
                    {categories.map((cat) => (
                        <MenuItem key={cat.id} value={cat.id}>
                        {cat.category_name}
                        </MenuItem>
                    ))}
                </InputField>
                <InputField
                    label="Preço"
                    value={value}
                    type='number'
                    onChange={(e) => setValue(e.target.value)}
                />
                <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
                    <Button type="submit" variant="contained" fullWidth disabled={!isFormValid()}>
                        {serviceToEdit ? 'Atualizar Serviço' : 'Cadastrar Serviço'}
                    </Button>
                    <Button onClick={handleCancel} variant="outlined" fullWidth>
                        Cancelar
                    </Button>
                </Box>
            </Form>
        </>
    );
};

export default ServiceForm;
