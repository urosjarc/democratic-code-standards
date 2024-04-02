<script>

    import {OpenAPI} from "$lib/api";

    OpenAPI.BASE = "http://0.0.0.0:8080"
    OpenAPI.HEADERS = {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }

    function init() {

    }
</script>

<div class="app">
    <a href="/feedback">Feedback</a>
    <a href="/languages">Languages</a>
    <a href="/linters">Linters</a>
    <a href="/rules">Rules</a>
    <a href="/user">User</a>

    <slot/>
</div>
