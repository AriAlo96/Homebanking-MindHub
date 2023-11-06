const app = Vue.createApp({
    data() {
        return {
            loans: [],
            loanId: 0,
            amount: 0,
            payments:0,
            destinationAccount: "",
            accounts: {}
        };
    },

    created() {
        axios.get("/api/loans")
        .then(response=>{
        this.loans = response.data
        console.log(this.loans);
        })
        .catch(error => console.log(error))

        axios.get("/api/clients/current/accounts")
            .then(response => {
                this.accounts = response.data;
                console.log(this.accounts);
            })
            .catch(error => {
                console.log(error);
            });
    },

    methods: {
        applyForLoan(){
            Swal.fire({
                title: 'Do you confirm your application for the loan?',
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
            axios.post("/api/loans",{"loanId":`${this.loanId}`, "amount":`${this.amount}`, "payments": `${this.payments}`, "destinationAccount": `${this.destinationAccount}`})
            .then(() => {
                Swal.fire({
                    title: "Successful loan application",
                    icon: "success",
                    confirmButtonColor: "#3085d6",
                  }).then((result) => {
                    if (result.isConfirmed) {
                        location.pathname = `/web/assets/pages/loan-application.html`;
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
        }
    }

})
app.mount('#app');