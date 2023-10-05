const app = Vue.createApp({
    data() {
        return {
            client: {},
            accounts: {},
        };
    },

    created() {
        axios.get("/api/clients/1")
            .then(response => {
                this.client = response.data;
                this.accounts = this.client.accounts
            })
            .catch(error => {
                console.log(error);
            });
    },

    methods: {
        formatNumber(number) {
            return number
                .toFixed(2)
                .replace(/\./g, ",")
                .replace(/\B(?=(\d{3})+(?!\d))/g, ".");
        }
    }
},
);
app.mount('#app');