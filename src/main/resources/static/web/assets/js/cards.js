const app = Vue.createApp({
    data() {
        return {
            client: {},
            cards: [],
            creditCards: [],
            debitCards: []
        };
    },

    created() {
        axios.get("/api/clients/current")
            .then(response => {
                this.client = response.data;
                this.cards = this.client.cards
                console.log(this.cards);
                this.creditCards = this.createCreditCards()
                this.debitCards = this.createDebitCards()
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
        createCreditCards(){
            return this.cards.filter(card => card.type == "CREDIT")
        },
        createDebitCards(){
            return this.cards.filter(card => card.type == "DEBIT")
        }
    }
},
);
app.mount('#app');