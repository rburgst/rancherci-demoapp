def findIpAddressForContainerStartingWith(prefix) {
    def jqImage = docker.image('rancher-server:5000/endeveit/docker-jq')
    jqImage.pull()

    String ipAddress = 'rancher-agent-01'
    jqImage.inside  {
        String cmdline = 'curl http://rancher-server:8080/v2-beta/containers | jq \'.data[] | select(.name | startswith("'
        cmdline += prefix
        cmdline += '")) | .data.dockerContainer.Labels."io.rancher.container.ip"\''
        ipAddress = sh(
                script: cmdline,
                returnStdout: true
        ).trim()
    }
    ipAddress = ipAddress.replaceAll('"', '')
    ipAddress = ipAddress.replaceFirst('/.*', '')

    ipAddress
}

return this