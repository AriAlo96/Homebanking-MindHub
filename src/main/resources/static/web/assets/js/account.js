const app = Vue.createApp({
    data() {
        return {
            client: {},
            account: {},
            transactions: {},
            messageError: "",
            email: ""
        };
    },

    created() {
        let urlParams = new URLSearchParams(location.search);
        let id = urlParams.get('id')     
        axios.get(`/api/accounts/${id}`)
            .then(response => {
                this.account = response.data;
                this.transactions = this.account.transactions
                this.transactions.sort((a, b) => b.id - a.id);
                console.log(this.transactions);
            })
            .catch(error => {
                this.messageError = error.response.data;
            });
        
        axios.get("/api/clients/current")
            .then(response => {
                this.client = response.data;
                this.email = this.client.email
            })
            .catch(error => {
                console.log(error);
            });
    },

    methods: {
        logout() {
            axios
                .post(`/api/logout`)
                .then(response => {
                    console.log("SingOut");
                    location.href = `http://localhost:8080/index.html`;
                })
                .catch(error => {
                    console.log(error);
                });
        },

        formatNumber(number) {
            return number.toLocaleString("De-DE", {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
            });
        },

        dateFormat(dateString) {
            const date = new Date(dateString);
            const formatOptions = { year: 'numeric', month: '2-digit', day: '2-digit' };
            return date.toLocaleDateString('es-ES', formatOptions);
        }
}   

},
);
app.mount('#app');