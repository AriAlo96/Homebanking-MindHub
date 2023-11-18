const app = Vue.createApp ({
    data(){
        return{
            loans: [],
            loanName: "",
            maxAmount: 0,
            payments: [],
            interestPercentage: 0
        };
    },
    created (){
        axios.get("/api/loans")
        .then(response=>{
        this.loans = response.data
        console.log(this.loans);
        })
        .catch(error => console.log(error))
    },
    methods:{
        createNewLoan(){
            Swal.fire({
                title: 'Confirm the creation of this loan?',
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
            axios.post("/api/admin/loans",`name=${this.loanName}&maxAmount=${this.maxAmount}&payments=${this.payments}&interestPercentage=${this.interestPercentage}`)
            .then(() => {
                Swal.fire({
                    title: "Loan created successfully",
                    icon: "success",
                    confirmButtonColor: "#3085d6",
                  }).then((result) => {
                    if (result.isConfirmed) {
                        location.pathname = `/administrator/assets/pages/loans.html`;
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
    }
});
app.mount('#app');