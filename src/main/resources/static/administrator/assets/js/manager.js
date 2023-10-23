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
            axios.get("/api/clients")
            .then(response => {
                this.jsonResponse = response.data; 
                this.clients = response.data;
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
            axios.post("/rest/clients", clientData)
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