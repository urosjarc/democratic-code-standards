<script>
    import {Button, Container, FormGroup, Input} from "@sveltestrap/sveltestrap";
    import {OpenAPI, UserService} from "$lib/api";

    function login() {

        UserService.postUserLogin({requestBody: {password, email}}).then((res) => {
            OpenAPI.TOKEN = res.token
            console.log(res.token)
        })
    }

    function user(){
        UserService.getUser().then((res) => {
            console.log(res)
        })
    }

    let email = ""
    let password = ""
</script>

<Container sm>
    <h1>User</h1>
    <FormGroup>
        <Input bind:value={email} type="email" placeholder="email placeholder"/>
        <Input bind:value={password} type="password" placeholder="password placeholder"/>
        <Button block on:click={login}>Login</Button>
        <Button on:click={user}>User</Button>
    </FormGroup>
</Container>
