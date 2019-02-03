import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';

class ReceiveMessageWidget extends StatelessWidget {
  final IOWebSocketChannel channel;

  const ReceiveMessageWidget({Key key, this.channel}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    if (channel != null) {
      return StreamBuilder(
        stream: channel.stream,
        builder: (context, snapshot) {
          return Padding(
            padding: const EdgeInsets.symmetric(vertical: 24.0),
            child: Text(snapshot.hasData ? '${snapshot.data}' : ''),
          );
        },
      );
    } else {
      return Container();
    }
  }
}
