import axios from 'axios';

const API_URL = 'https://pet-service-api-d0hxdharh6d3bgd2.eastus2-01.azurewebsites.net/';

const login = async (credentials) => {
    let endpoint;
    if (credentials.userType === "tutor") {
        endpoint = 'pet-guardian/auth/authenticate';
    } else {
        endpoint = 'pet-establishment/auth/authenticate';
    }
    const response = await axios.post(API_URL + endpoint, credentials);
    return response.data;
};

const getUserData = async (userType, token = localStorage.getItem('authToken')) => {
    let endpoint;
    if (userType === "tutor") {
        endpoint = 'pet-guardian/me';
    } else {
        endpoint = 'pet-establishment/me';
    }
    try {
        const response = await fetch(API_URL + endpoint, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao obter dados do usuário');
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};

const updateUserData = async (userId, userData) => {
    let endpoint;
    if (userData.type === "tutor") {
        endpoint = `pet-guardian/${userId}`;
    } else {
        endpoint = `pet-establishment/${userId}`;
    }
    let response;
    try {
        response = await axios.put(API_URL + endpoint, userData, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
            },
        });
    } catch (error) {
        console.log(error);
    }
    return response.data;
};

const getAllServices = async (index, itemsPerPage) => {
    const endpoint = `service-provided/${index}/${itemsPerPage}`;
    const response = await fetch(API_URL + endpoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error(response.error);
    }

    return await response.json();
};

const getEstablishmentServices = async (userId, index, itemsPerPage) => {
    const endpoint = `service-provided/pet-establishment/${userId}/${index}/${itemsPerPage}`;
    const response = await fetch(API_URL + endpoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
            'Content-Type': 'application/json',
        },
    });
    if (!response.ok) {
        throw new Error(response.error);
    }
    return await response.json();
};

const getAllEstablishmentServices = async (establishmentId) => {
    let index = 0;
    const itemsPerPage = 50;
    let allEstablishmentServices = [];
    let hasMore = true;

    try {
        while (hasMore) {
            const establishmentServices = await getEstablishmentServices(establishmentId, index, itemsPerPage);
            allEstablishmentServices = [...allEstablishmentServices, ...establishmentServices?.content];

            hasMore = establishmentServices.length === itemsPerPage;
            index += 1;
        }
    } catch (error) {
        console.error("Erro ao obter todos os serviços do estabelecimento: ", error);
        throw error;
    }

    return allEstablishmentServices;
}

const getAllEstablishments = async (index, itemsPerPage) => {
    const endpoint = `pet-establishment/${index}/${itemsPerPage}`;
    try {
        const response = await fetch(API_URL + endpoint, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            const errorMessage = await response.text(); // Captura o corpo do erro retornado
            console.error(`Erro na resposta do servidor: ${response.status} - ${errorMessage}`);
            throw new Error(`Erro ao carregar estabelecimentos: ${errorMessage}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Erro ao obter estabelecimentos:", error); // Log completo do erro
        throw new Error(`Erro ao obter estabelecimentos: ${error.message}`);
    }
};

const getPetsByTutor = async (userId, index, itemsPerPage) => {
    const endpoint = `pet/pet-guardian/${userId}/${index}/${itemsPerPage}`;
    const response = await fetch(API_URL + endpoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
            'Content-Type': 'application/json',
        },
    });
    if (!response.ok) {
        throw new Error(response.error);
    }
    return await response.json();
};

const getAllPetsByTutor = async (tutorId) => {
    let index = 0;
    const itemsPerPage = 50;
    let allPets = [];
    let hasMore = true;

    try {
        while (hasMore) {
            const pets = await getPetsByTutor(tutorId, index, itemsPerPage);
            allPets = [...allPets, ...pets?.content];

            hasMore = pets.length === itemsPerPage;
            index += 1;
        }
    } catch (error) {
        console.error("Erro ao obter todos os pets do tutor: ", error);
        throw error;
    }

    return allPets;
}

const getCategories = async (index, itemsPerPage) => {
    const endpoint = `category-tag/${index}/${itemsPerPage}`
    try {
        const response = await fetch(API_URL + endpoint, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar categorias');
        }

        const data = await response.json();
        return data.content;
    } catch (error) {
        console.error("Erro ao obter categorias paginadas: ", error);
        throw error;
    }
};

const getAllCategories = async () => {
    let index = 0;
    const itemsPerPage = 50;
    let allCategories = [];
    let hasMore = true;

    try {
        while (hasMore) {
            const categories = await getCategories(index, itemsPerPage);
            allCategories = [...allCategories, ...categories];

            hasMore = categories.length === itemsPerPage;
            index += 1;
        }
    } catch (error) {
        console.error("Erro ao obter todas as categorias: ", error);
        throw error;
    }

    return allCategories;
}

const addService = async (newService) => {
    const endpoint = 'service-provided';
    try {
        const response = await axios.post(API_URL + endpoint, newService, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            },
        });
        return response.data;
    } catch (error) {
        console.error('Erro ao adicionar serviço:', error);
        throw error;
    }
};

const updateService = async (serviceId, updatedService) => {
    const endpoint = `service-provided/${serviceId}`;
    try {
        const response = await axios.put(API_URL + endpoint, updatedService, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            },
        });
        return response.data;
    } catch (error) {
        console.error('Erro ao atualizar serviço:', error);
        throw error;
    }
};

const getAppointmentServicesByUser = async (userType, userId) => {
    let endpoint;
    if (userType === "tutor") {
        endpoint = `appointment/pet-guardian/${userId}`;
    } else {
        endpoint = `appointment/pet-establishment/${userId}`;
    }
    const response = await fetch(API_URL + endpoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error(response.error);
    }

    return await response.json();
}

const getAllConversations = async (userId) => {
    const endpoint = 'message/last';
    const response = await fetch(API_URL + endpoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error(response.error);
    }

    const result = await response.json();
    const filteredMessages = result.filter((msg) => msg.sender_id === userId || msg.receiver_id === userId);
    await Promise.all(filteredMessages.map(async (msg) => {
        if (msg.sender_id === userId) {
            const resp = await getUserNameAndType(msg.receiver_id);
            msg.partner = {
                id: resp.id,
                name: resp.name,
                type: resp.type
            }
        } else if (msg.receiver_id === userId) {
            const resp = await getUserNameAndType(msg.sender_id);
            msg.partner = {
                id: resp.id,
                name: resp.name,
                type: resp.type
            }
        }
    }));
    return filteredMessages.reverse();
}

const getAllMessages = async (combo) => {
    const endpoint = `message/${combo}`;
    const partnersId = combo.split('-');
    const response = await fetch(API_URL + endpoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error(response.error);
    }

    const result = await response.json();
    const messages = Object.entries(result).map(([id, body]) => {
        const index = body.indexOf(': ');
        const sender_id = body.substring(0, index);
        const msg = body.substring(index + 2);
        return {
            id: id,
            combo: combo,
            sender_id: sender_id,
            receiver_id: partnersId[0] == sender_id
                ? partnersId[1]
                : partnersId[0],
            msg: msg
        };
    });
    return messages;
}

const getUserNameAndType = async (userId) => {
    const establishmentEndpoint = `pet-establishment/${userId}`;

    try {
        const establishmentResponse = await fetch(API_URL + establishmentEndpoint, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (establishmentResponse.ok) {
            const establishmentUser = await establishmentResponse.json();
            return {
                id: establishmentUser.id,
                name: establishmentUser.business_name,
                type: 'empresa'
            };
        }

        const guardianEndpoint = `pet-guardian/${userId}`;

        const guardianResponse = await fetch(API_URL + guardianEndpoint, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (guardianResponse.ok) {
            const guardianUser = await guardianResponse.json();
            return {
                id: guardianUser.id,
                name: guardianUser.full_name,
                type: 'tutor'
            };
        }

        throw new Error('Usuário não encontrado em nenhum dos registros.');
    } catch (error) {
        console.error('Erro ao obter dados do usuário:', error.message);
        throw error;
    }
}

const getAppointments = async (index, itemsPerPage) => {
    const endpoint = `appointment/${index}/${itemsPerPage}`
    try {
        const response = await fetch(API_URL + endpoint, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar agendamentos');
        }

        const data = await response.json();
        console.log('appointments: ', data);
        return data.content;
    } catch (error) {
        console.error("Erro ao obter agendamentos paginados: ", error);
        throw error;
    }
};

const getAllAppointments = async () => {
    let index = 0;
    const itemsPerPage = 50;
    let allAppointments = [];
    let hasMore = true;

    try {
        while (hasMore) {
            const appointments = await getAppointments(index, itemsPerPage);
            allAppointments = [...allAppointments, ...appointments];

            hasMore = appointments.length === itemsPerPage;
            index += 1;
        }
    } catch (error) {
        console.error("Erro ao obter todos os agendamentos: ", error);
        throw error;
    }

    return allAppointments;
}

const getAppointmentsByPetGuardian = async (guardinId) => {
    const endpoint = `appointment/pet-guardian/${guardinId}`
    try {
        const response = await fetch(API_URL + endpoint, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar agendamentos do tutor');
        }

        return await response.json();
    } catch (error) {
        console.error("Erro ao obter agendamentos do tutor: ", error);
        throw error;
    }
};

const getLastAppointmentsByPetGuardian = async (guardinId) => {
    const endpoint = `appointment/pet-guardian/${guardinId}`
    try {
        const response = await fetch(API_URL + endpoint, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar agendamentos do tutor');
        }

        return (await response.json()).pop();
    } catch (error) {
        console.error("Erro ao obter agendamentos do tutor: ", error);
        throw error;
    }
};

const acceptAppointment = async (appointmentData) => {
    const endpoint = `appointment`;
    try {
        const response = await axios.post(API_URL + endpoint, appointmentData, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.status !== 201) {
            throw new Error('Erro ao aceitar o agendamento');
        }

        const appointmentId = response.data.id;
        
        return response.data;
    } catch (error) {
        console.error('Erro ao aceitar agendamento:', error.message);
        throw error;
    }
};

const updateMessage = async (messageId, data) => {
    const endpoint = `message/${messageId}`;
    try {
        const response = await axios.put(API_URL + endpoint, data, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
            },
        });
        return response.data;
    } catch (error) {
        console.log(error);
    }
};

const authService = {
    login,
    getUserData,
    updateUserData,
    getAllServices,
    getEstablishmentServices,
    getAllEstablishmentServices,
    getPetsByTutor,
    getAllPetsByTutor,
    getCategories,
    getAllCategories,
    addService,
    updateService,
    getAppointmentServicesByUser,
    getAllEstablishments,
    getAllConversations,
    getAllMessages,
    getUserNameAndType,
    getAppointments,
    getAllAppointments,
    getAppointmentsByPetGuardian,
    getLastAppointmentsByPetGuardian,
    acceptAppointment,
    updateMessage,
};

export default authService;
