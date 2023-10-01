const app = Vue.createApp ({
    data(){
        return{
            clients: [],
            newClient: {
                firstName: "",
                lastName: "",
                email:""
            },
            jsonResponse: "",
            successMessage:""
        };
    },
    created (){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get("/clients")
            .then(response => {
                this.clients = this.clients = response.data._embedded.clients;
                this.jsonResponse = response.data;
            })
            .catch(error => {
                console.log(error);
            });
        },
        
        addClient(){
            if (this.newClient.firstName && this.newClient.lastName && this.newClient.email){
                this.postClient(this.newClient);
            } else {
                alert("Please enter all required data")
            }
        },
        postClient(clientData){
            axios.post("/clients", clientData)
            .then (() => {
                this.loadData();
            })
            .catch(error => {
                console.log(error);
            });
        }
    }
});
app.mount('#app');