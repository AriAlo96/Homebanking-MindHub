const app = Vue.createApp({
    data() {
        return {
            client: {},
            accounts: {}, 
            loans: {}, 
            amount: 0, 
            idLoan: 0,
            idAccount: 0,
            email: ""
        };
    },

    created() {
        axios.get("/api/clients/current")
        .then(response=>{
        this.client = response.data;
        this.email = this.client.email
        console.log(this.client);
        this.loans = this.client.loans;
        this.accounts = this.client.accounts;

        })
        .catch(error => console.log(error))

    },

    methods: {
        payLoan(){
            Swal.fire({
                title: 'Do you confirm the payment of an installment of your loan?',
                text: 'It will be deducted from the selected account',
                showCancelButton: true,
                cancelButtonText: 'Cancell',
                confirmButtonText: 'Confirm',
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
                console.log(this);
            axios.post(`/api/loans/payments`, `idLoan=${this.idLoan}&idAccount=${this.idAccount}&amount=${this.amount}`)
                .then(() => {
                    Swal.fire({
                        title: "Payment made successfully",
                        icon: "success",
                        confirmButtonColor: "#3085d6",
                      }).then((result) => {
                        if (result.isConfirmed) {
                            location.pathname = `/web/assets/pages/payLoan.html`;
                        }
                      });    
                    
                })
                .catch(error => {
                    Swal.fire({
                      icon: 'error',
                      text: error.response.data,
                      confirmButtonColor: "#7c601893",
                      
                    });
                    console.log(error);
            });
            },
        })
        },

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