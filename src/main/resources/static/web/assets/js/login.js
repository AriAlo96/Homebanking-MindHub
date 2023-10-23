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
        };
    },

    created() {

    },
    methods: {
        login() {
            axios
                .post('/api/login', `email=${this.email}&password=${this.password}`)
                .then(response => {
                    location.href = `http://localhost:8080/web/accounts.html`;
                })
                .catch(error => {
                    console.log(error);
                    this.errorMessage = "Incorrect credentials. Please try again."
                });
        },

        register(){
            axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`)
            .then(response => {
                location.href = `http://localhost:8080/web/accounts.html`
            })
            .catch(error => {
                console.log(error);
            });
        }
    }
})
app.mount('#app');