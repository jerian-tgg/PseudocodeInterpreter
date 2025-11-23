/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.interpreter;

/**
 *
 * @author Josh
 */
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    private final Environment parent;

    public Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public boolean has(String name) {
        if (values.containsKey(name)) {
            return true;
        }
        if (parent != null) {
            return parent.has(name);
        }
        return false;
    }

    public void set(String name, Object value) {
        // If variable exists in current or parent scope, update it
        if (values.containsKey(name)) {
            values.put(name, value);
        } else if (parent != null && parent.has(name)) {
            parent.set(name, value);
        } else {
            // Create new variable in current scope
            values.put(name, value);
        }
    }

    public Object get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }
        if (parent != null) {
            return parent.get(name);
        }
        return null;
    }

    public Map<String, Object> snapshot() {
        return new HashMap<>(values);
    }

    public Environment createChild() {
        return new Environment(this);
    }
}
