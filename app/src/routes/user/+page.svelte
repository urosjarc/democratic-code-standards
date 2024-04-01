<script>
    import {Button, Container, FormGroup, Input} from "@sveltestrap/sveltestrap";

    function login() {
        alert(email)
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
    </FormGroup>
</Container>
