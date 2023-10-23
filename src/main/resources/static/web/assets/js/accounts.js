const app = Vue.createApp({
    data() {
        return {
            client: {},
            accounts: {},
            loans: {}
        };
    },

    created() {
        axios.get("/api/clients/current")
            .then(response => {
                this.client = response.data;
                this.accounts = this.client.accounts;
                this.loans = this.client.loans
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
    }
},
);
app.mount('#app');