# Protocol Buffers

In practice, protobufs are likely to be from a shared project. Perhaps you want to import your protobufs into your project as a Git submodule and symlink specific files into the Scala app.

Make sure that the relevant protobufs are also copied into the gateway. You can do that through the gateway's [Makefile](../gateway/Makefile). Symlinks won't work here because Dockerfiles don't support COPYing symlinked resources.