import 'package:flutter/material.dart';
import 'package:meta/meta.dart';

import 'unit.dart';

class Category {
  final String name;
  final ColorSwatch color;
  final List<Unit> units;
  final IconData icon;

  const Category({
    @required this.name,
    @required this.color,
    @required this.units,
    @required this.icon,
  })  : assert(name != null),
        assert(color != null),
        assert(units != null),
        assert(icon != null);
}