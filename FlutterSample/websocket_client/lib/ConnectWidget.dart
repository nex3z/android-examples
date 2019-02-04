import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/status.dart' as status;

typedef void OnConnectedListener(IOWebSocketChannel channel);
typedef void OnDisconnectedListener();

class ConnectWidget extends StatefulWidget {
  final OnConnectedListener onConnected;
  final OnDisconnectedListener onDisconnected;

  ConnectWidget({Key key, this.onConnected, this.onDisconnected}) : super(key: key);

  @override
  State createState() => _ConnectWidgetState();
}

class _ConnectWidgetState extends State<ConnectWidget> {
  final _addressController = TextEditingController(text: 'ws://echo.websocket.org');
  IOWebSocketChannel _channel;

  Widget _buildConnectButton() {
    if (_channel == null) {
      return RaisedButton(
        child: Text('Connecct'),
        onPressed: _connect,
      );
    } else {
      return RaisedButton(
        child: Text('Disconnecct'),
        onPressed: _disconnect,
      );
    }
  }

  void _connect() {
    var address = _addressController.text;
    print("address = $address");
    setState(() {
      _channel = IOWebSocketChannel.connect(address);
    });
    widget.onConnected(_channel);
  }

  void _disconnect() {
    setState(() {
      _channel.sink.close(status.normalClosure);
      _channel = null;
    });
    widget.onDisconnected();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Flexible(
          flex: 1,
          child: TextField(
            style: Theme.of(context).textTheme.body1,
            decoration: InputDecoration(
              labelText: 'Address',
            ),
            controller: _addressController,
          ),
        ),
        Flexible(
          flex: 0,
          child: _buildConnectButton(),
        )
      ],
    );
  }
}
