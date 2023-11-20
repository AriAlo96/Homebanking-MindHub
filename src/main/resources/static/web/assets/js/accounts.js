const app = Vue.createApp({
    data() {
        return {
            client: {},
            accounts: {},
            loans: {},
            accountType: null,
            email: ""
        };
    },

    created() {
        axios.get("/api/clients/current")
            .then(response => {
                this.client = response.data;
                this.email = this.client.email
                this.accounts = this.client.accounts;
                this.accounts.sort((a, b) => a.id - b.id);
                console.log(this.accounts);
                this.loans = this.client.loans
                console.log(this.loans);
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
                    location.pathname = `/index.html`;
                })
                .catch(error => {
                    console.log(error);
                });
        },

        createNewAccount() {
            Swal.fire({
                title: 'Do you want to create a new account?',
                text: 'Remember that you can only have 3 accounts',
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
            axios.post(`/api/clients/current/accounts`,`accountType=${this.accountType}`)
                .then(() => {
                    Swal.fire({
                        title: "Successfully created account",
                        icon: "success",
                        confirmButtonColor: "#3085d6",
                      }).then((result) => {
                        if (result.isConfirmed) {
                            location.pathname = `/web/accounts.html`;
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

    deleteAccount(id) {
        Swal.fire({
            title: 'Do you want to delete account?',
            text: 'This action cannot be reversed',
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
        axios.patch(`/api/clients/current/accounts`, `id=${id}`)
            .then(() => {
                Swal.fire({
                    title: "Successfully delete account",
                    icon: "success",
                    confirmButtonColor: "#3085d6",
                  }).then((result) => {
                    if (result.isConfirmed) {
                        location.pathname = `/web/accounts.html`;
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
    }
},
);
app.mount('#app');