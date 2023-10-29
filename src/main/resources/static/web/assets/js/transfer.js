const app = Vue.createApp({
    data() {
        return {
            accounts: {},  
            amount: 0,
            description: "",
            originNumber: "",
            destinationNumber: ""   
        };
    },

    created() {
        axios.get("/api/clients/current/accounts")
            .then(response => {
                this.accounts = response.data;
            })
            .catch(error => {
                console.log(error);
            });
    },

    methods: {
        generateTransfer(){
            Swal.fire({
                title: 'Are you sure about making the transfer?',
                text: 'It is not cancelable',
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
            axios.post(`/api/clients/current/transfers`, `amount=${this.amount}&description=${this.description}&originNumber=${this.originNumber}&destinationNumber=${this.destinationNumber}`)
                .then(() => {
                    Swal.fire({
                        icon: 'success',
                        text: 'Successfully created account',
                        showConfirmButton: true,
                        timer: 5000,
                    })
                    location.pathname = `/web/assets/pages/transfers.html`;
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