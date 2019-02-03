import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';

class SendMessageWidget extends StatefulWidget {
  final IOWebSocketChannel channel;

  const SendMessageWidget({Key key, this.channel}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _SendMessageWidgetState();
}

class _SendMessageWidgetState extends State<SendMessageWidget> {
  final _messageController = TextEditingController(text: 'Hello');

  void _send() {
    if (widget.channel == null) {
      return;
    }
    var message = _messageController.text;
    print("message = $message");
    widget.channel.sink.add(message);
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Flexible(
          child: TextField(
            style: Theme.of(context).textTheme.body1,
            decoration: InputDecoration(
              labelText: 'Message',
            ),
            controller: _messageController,
          ),
        ),
        Flexible(
          flex: 0,
          child: RaisedButton(
            child: Text('Send'),
            onPressed: _send,
          ),
        )
      ],
    );
  }
}
