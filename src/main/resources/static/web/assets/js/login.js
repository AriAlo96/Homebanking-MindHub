const app = Vue.createApp({
    data() {
        return {
            email: "",
            password: "",
            errorMessage: "",
            firstName: "",
            lastName: "",
            emailRegister: "",
            passwordRegister: "",
            exitMessage: "",
            errorStatus: null,
            errorRegister: ""
        };
    },

    created() {

    },
    methods: {
        login() {
            axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                .then(response => {
                    location.pathname = `/web/accounts.html`;
                })
                .catch(error => {
                    if (error.response && error.response.status === 401) {
                        this.errorStatus = 401
                        this.errorMessage = "Incorrect credentials. Please try again."
                    } else {
                        this.errorStatus = null
                    }
                });
        },

        register() {
            axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`)
                .then(() => {
                    axios.post('/api/login', `email=${this.emailRegister}&password=${this.passwordRegister}`)
                        .then(() => location.pathname = `/web/accounts.html`)
                        .catch(error => {
                            console.log(error);
                        });
                })
                .catch(error => {
                    let errorMessage = error.response.data;
                    errorMessage = errorMessage.replace(/\n/g, '<br>');
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        html: errorMessage,
                    });
            });

        }
    }
})
app.mount('#app');