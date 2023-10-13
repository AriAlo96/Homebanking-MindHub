const app = Vue.createApp({
    data() {
        return {
            client: {},
            accounts: {},
            loans: {}
        };
    },

    created() {
        axios.get("/api/clients/1")
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
        formatNumber(number) {
            return number.toLocaleString("De-DE", {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
            });
        }
    }
},
);
app.mount('#app');