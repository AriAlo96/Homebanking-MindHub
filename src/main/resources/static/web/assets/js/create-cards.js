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
                this.creditCards = this.createCreditCards()
                this.debitCards = this.createDebitCards()
            })
            .catch(error => {
                console.log(error);
            });
    },

    methods: {
        logout() {
            Swal.fire({
                title: 'Are you sure you want to log out?',
                text: 'You will need to log in again to access your accounts',
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
            axios
                .post(`/api/logout`)
                .then(() => {
                    Swal.fire({
                        title: "Successfully logout",
                        icon: "success",
                        confirmButtonColor: "#3085d6",
                      }).then((result) => {
                        if (result.isConfirmed) {
                            location.pathname = `/index.html`;
                        }
                      });      
                })
                .catch(error => {
                    Swal.fire({
                      icon: 'error',
                      text: error.response.data,
                      confirmButtonColor: "#7c601893",
                    });
            });
            }})
            
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
                        title: "Successfully created card",
                        icon: "success",
                        confirmButtonColor: "#3085d6",
                      }).then((result) => {
                        if (result.isConfirmed) {
                            location.pathname = `/web/assets/pages/cards.html`;
                        }
                      });      
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
            return this.cards.filter(card => card.type == "CREDIT" && card.active)
        },
        createDebitCards(){
            return this.cards.filter(card => card.type == "DEBIT" && card.active)
        }
    }
},
);
app.mount('#app');