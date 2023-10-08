const app = Vue.createApp({
    data() {
        return {
            account: {},
            transactions: {},
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
                console.log(error);
            });
    },

    methods: {
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