const app = Vue.createApp({
    data() {
        return {
            cards: [],
            creditCards: [],
            debitCards: [],
            cardType: "",
            cardColor: ""
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
                    location.pathname = `/index.html`;
                })
                .catch(error => {
                    console.log(error);
                });
        },

        createNewCard() {
            Swal.fire({
                title: 'Do you want to create a new card?',
                text: 'Remember that you can only have 3 cards',
                showCancelButton: true,
                cancelButtonText: 'Cancell',
                confirmButtonText: 'Yes',
                confirmButtonColor: '#28a745',
                cancelButtonColor: '#dc3545',
                showClass: {
                  popup: 'swal2-noanimation',
                  backdrop: 'swal2-noanimation'
                },
                hideClass: {
                  popup: '',
                  backdrop: ''
            }, preConfirm: () => {
            axios.post(`/api/clients/current/cards`, `type=${this.cardType}&color=${this.cardColor}`)
                .then(() => {
                    Swal.fire({
                        icon: 'success',
                        text: 'Successfully created account',
                        showConfirmButton: false,
                        timer: 2000,
                    })
                    location.pathname = `/web/assets/pages/cards.html`;
                })
                .catch(error => {
                    Swal.fire({
                      icon: 'error',
                      text: error.response.data,
                      confirmButtonColor: "#7c601893",
                    });
            });
            },
        })
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